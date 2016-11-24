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

    Button botonRegistrar, agregar;
    TextView stock, codigoProd, clientePantalla,nroPedido, total, direccion, email, precioUnit;
    TableLayout tableLayout;

    int pedido, p, suma, posicionProducto, codigoClient, bandera, stockAlert, stockArray;
    int cantidad = 1;
    String n;

    OperacionesBaseDatos datos;
    Cursor a, c, cu, cli;
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

        producto = (Spinner) findViewById(R.id.spinner);
        nroPedido = (TextView) findViewById(R.id.textViewNumeroPedido);
        clientePantalla = (TextView) findViewById(R.id.textViewCliente);
        cant = (Spinner) findViewById(R.id.spinner2);
        total = (TextView) findViewById(R.id.textView);
        direccion = (TextView) findViewById(R.id.textViewDireccion);
        email = (TextView) findViewById(R.id.textViewEmail);
        precioUnit = (TextView) findViewById(R.id.textViewPrecio);
        stock = (TextView) findViewById(R.id.textViewStock);
        codigoProd = (TextView) findViewById(R.id.textViewCodigo);
        suma= 0;
        total.setText(String.valueOf(suma) + " Gs.");



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
            cli = datos.obtenerClientesPorId(String.valueOf(codigoClient));
            cli.moveToFirst();
            direccion.setText(cli.getString(4));
            email.setText(cli.getString(5));
            String cliente = intent.getStringExtra("cliente");
            clientePantalla.setText(cliente);
        }

        cu= datos.obtenerProductos();
        String[] array = new String[cu.getCount() +1];
        array[0] = "Seleccione un Producto...";
        int i = 1;
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
        producto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) view).setTextColor(Color.BLACK);
                if (position == 0) {
                    bandera = 1;
                    stockArray = 1;
                    codigoProd.setText("****");
                    stock.setText("****");
                    precioUnit.setText("**** Gs.");

                    String[] a = new String[stockArray];
                    a[0] = "0";
                    final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, a);
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

                } else {
                    bandera = 0;
                    p = productoList.get(position - 1).precio_unitario;
                    n = productoList.get(position - 1).nom_producto;
                    posicionProducto = position - 1;
                    stockArray = productoList.get(position - 1).stock_actual + 1;

                    codigoProd.setText(String.valueOf(productoList.get(position - 1).id_producto));
                    stock.setText(String.valueOf(productoList.get(position - 1).stock_actual));
                    precioUnit.setText(String.valueOf(productoList.get(position - 1).precio_unitario) + " Gs.");

                    String[] a = new String[stockArray];
                    if (stockArray == 1) {
                        a[0] = "Sin Stock";
                    } else {
                        for (int x = 0; x < stockArray; x++) {
                            a[x] = String.valueOf(x);
                        }
                    }
                        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, a);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        cant.setAdapter(adapter1);
                        cant.setSelection(0);
                        cant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if(position > 0){
                                    cantidad = Integer.parseInt(adapter1.getItem(position));
                                    stockAlert = 0;
                                }else if (adapter1.getItem(position) == "Sin Stock") {
                                    stockAlert = 1;
                                }
                                ((TextView) view).setTextColor(Color.BLACK);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                }
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

                if(detalleList.isEmpty()){

                    Toast.makeText(getApplicationContext(), "Debe Agregar al menos un Producto!!!", Toast.LENGTH_SHORT).show();

                }else{

                    venta = new Venta(null, null , suma, 1, codigoClient);
                    String resultadoVenta, resultadoDetalleVenta = new String();
                    try {
                        datos.getDb().beginTransaction();
                        resultadoVenta= datos.insetarVenta(venta);
                        datos.getDb().setTransactionSuccessful();

                    } finally {
                        datos.getDb().endTransaction();
                    }

                    try {
                        datos.getDb().beginTransaction();
                        for(int x = 0; x < detalleList.size(); x++){
                            resultadoDetalleVenta= datos.insertarDetalleVenta(detalleList.get(x));
                            a= datos.obtenerProductosId(String.valueOf(detalleList.get(x).id_producto));
                            a.moveToFirst();
                            int z = a.getInt(3);
                            datos.actualizarCantidad(z-detalleList.get(x).cantidad, detalleList.get(x).id_producto);
                            if(resultadoDetalleVenta == "-1") break;

                        }
                        datos.getDb().setTransactionSuccessful();

                    } finally {
                        datos.getDb().endTransaction();
                    }

                    if(resultadoVenta == "-1" || resultadoDetalleVenta == "-1"){
                        Toast.makeText(getApplicationContext(), "Error al Registrar el pedido", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Pedido Registrado Exitosamente", Toast.LENGTH_LONG).show();
                    }
                    producto.setSelection(0);
                    cant.setSelection(0);
                    tableLayout.removeViews(1, tableLayout.getChildCount() -1);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                break;

            case R.id.buttonAgregar:

                if(bandera == 1){
                    Toast.makeText(getApplicationContext(),"Debe seleccionar un Producto!!!", Toast.LENGTH_SHORT).show();;
                }else if(cantidad == 0 && stockAlert==0){
                    Toast.makeText(getApplicationContext(), "Debe seleccionar una Cantidad!!!", Toast.LENGTH_SHORT).show();
                }else if (stockAlert == 1){
                    Toast.makeText(getApplicationContext(), "Producto fuera de Stock!!!", Toast.LENGTH_SHORT).show();
                }else{
                    d = new DetalleVenta(null , pedido, p * cantidad, productoList.get(posicionProducto).id_producto, cantidad);
                    detalleList.add(d);
                    productoList.get(posicionProducto).stock_actual-=cantidad;
                    suma=0;
                    for (int con = 0; con < detalleList.size(); con++){
                        suma+= detalleList.get(con).sub_total;
                    }

                    tableLayout = (TableLayout) findViewById(R.id.table);
                    TableRow row = new TableRow(this);
                    TextView nomProducto = new TextView(this);
                    nomProducto.setText(n);
                    TextView precio = new TextView(this);
                    precio.setText(String.valueOf(p) + " Gs.");
                    TextView canti = new TextView(this);
                    canti.setText(String.valueOf(cantidad));
                    TextView sub = new TextView(this);
                    sub.setText(String.valueOf(p*cantidad) + " Gs.");
                    row.addView(nomProducto);
                    row.addView(precio);
                    row.addView(canti);
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

                            productoList.get(detalleList.get(posicion-1).id_producto -1).stock_actual+= detalleList.get(posicion-1).cantidad;
                            detalleList.remove(posicion -1);
                            suma = 0;
                            for (int con = 0; con < detalleList.size(); con++){
                                suma+= detalleList.get(con).sub_total;
                            }
                            total.setText(String.valueOf(suma));
                            producto.setSelection(0);
                            cant.setSelection(0);
                        }
                    });

                    tableLayout.addView(row,new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    producto.setSelection(0);
                    cant.setSelection(0);
                }

                break;
            default:
                break;

        }
    }

}
