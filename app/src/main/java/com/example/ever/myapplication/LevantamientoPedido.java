package com.example.ever.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.text.StringPrepParseException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ever.myapplication.modelo.DetalleVenta;
import com.example.ever.myapplication.modelo.Producto;
import com.example.ever.myapplication.modelo.Venta;
import com.example.ever.myapplication.sqlite.OperacionesBaseDatos;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class LevantamientoPedido extends AppCompatActivity implements View.OnClickListener{

    Button botonRegistrar;
    Button agregar;
    TextView clientePantalla;
    TextView nroPedido;
    TextView total;

    int pedido, p, suma, posicionProducto, codigoClient;
    int cantidad = 1;
    String n;

    OperacionesBaseDatos datos;
    Cursor c, cu;
    Spinner producto, cant;
    ArrayList<DetalleVenta> detalleList = new ArrayList<DetalleVenta>();
    ArrayList<Producto> productoList = new ArrayList<Producto>();
    DetalleVenta d;
    Producto prod;
    Venta venta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levantamiento_pedido);

        String[] a = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        producto = (Spinner) findViewById(R.id.spinner);
        nroPedido = (TextView) findViewById(R.id.textViewNumeroPedido);
        clientePantalla = (TextView) findViewById(R.id.textViewCliente);
        cant = (Spinner) findViewById(R.id.spinner2);
        total = (TextView) findViewById(R.id.textView);



        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());
        c = datos.obtenerVenta();
        c.moveToLast();

        if(c.getCount() == 0){
            pedido = 1;
        }else{
            pedido = c.getInt(0) + 1;
        }
        if(pedido < 10){
            nroPedido.setText(String.valueOf("000" + pedido));
        }else if(pedido > 10 && pedido < 100){
            nroPedido.setText(String.valueOf("00" + pedido));
        }else{
            nroPedido.setText(String.valueOf("0" + pedido));
        }
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            codigoClient = intent.getIntExtra("codigo", 0);
            String cliente = intent.getStringExtra("cliente");
            clientePantalla.setText(cliente);
        }

        cu= datos.obtenerProductos();
        String[] array = new String[cu.getCount()];
        int i = 0;
        if(cu.moveToFirst()){
            do{

                array[i] = cu.getString(1);
                prod = new Producto(cu.getInt(0),cu.getString(1), cu.getInt(2), cu.getInt(3));
                productoList.add(prod);
                i++;
            }while (cu.moveToNext());

        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        producto.setAdapter(adapter);
        producto.setSelection(0);

        final ArrayAdapter<String> adapter1 =  new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, a);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cant.setAdapter(adapter1);
        cant.setSelection(0);

        cant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cantidad = Integer.parseInt(adapter1.getItem(position));
                ((TextView) view).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        producto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                p= productoList.get(position).precio_unitario;
                n= productoList.get(position).nom_producto;
                posicionProducto = position;

               ((TextView) view).setTextColor(Color.BLACK);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        botonRegistrar= (Button) findViewById(R.id.ButtonRegistrar);
        botonRegistrar.setOnClickListener(this);

        agregar = (Button) findViewById(R.id.buttonAgregar);
        agregar.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ButtonRegistrar:
                venta = new Venta(null, null , suma, 1, codigoClient);
                try {
                    datos.getDb().beginTransaction();
                    datos.insetarVenta(venta);
                    datos.getDb().setTransactionSuccessful();

                } finally {
                    datos.getDb().endTransaction();
                }

                try {
                    datos.getDb().beginTransaction();
                    for(int x = 0; x < detalleList.size(); x++){
                        datos.insertarDetalleVenta(detalleList.get(x));
                    }
                    datos.getDb().setTransactionSuccessful();

                } finally {
                    datos.getDb().endTransaction();
                }

                Toast.makeText(getApplicationContext(), "Pedido Registrado", Toast.LENGTH_SHORT).show();

                break;
            case R.id.buttonAgregar:


                d = new DetalleVenta(null , pedido, p * cantidad, productoList.get(posicionProducto).id_producto, cantidad);
                detalleList.add(d);
                suma=0;
                for (int con = 0; con < detalleList.size(); con++){
                    suma+= detalleList.get(con).sub_total;
                }

                final TableLayout tableLayout = (TableLayout) findViewById(R.id.table);
                TableRow row = new TableRow(this);
                TextView nomProducto = new TextView(this);
                nomProducto.setText(n);
                TextView precio = new TextView(this);
                precio.setText(String.valueOf(p) + " Gs.");
                TextView cant = new TextView(this);
                cant.setText(String.valueOf(cantidad));
                TextView sub = new TextView(this);
                sub.setText(String.valueOf(p*cantidad) + " Gs.");
                row.addView(nomProducto);
                row.addView(precio);
                row.addView(cant);
                row.addView(sub);
                total.setText(String.valueOf(suma) + " Gs.");
                final Button button = new Button(this);
                button.setText("Borrar");
                button.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                row.addView(button);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final TableRow parent = (TableRow) v.getParent();
                        int posicion = tableLayout.indexOfChild(parent);
                        tableLayout.removeView(parent);

                        detalleList.remove(posicion -1);
                        suma = 0;
                        for (int con = 0; con < detalleList.size(); con++){
                            suma+= detalleList.get(con).sub_total;
                        }
                        total.setText(String.valueOf(suma));


                    }
                });

                tableLayout.addView(row,new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                break;
            default:
                break;

        }
    }

}
