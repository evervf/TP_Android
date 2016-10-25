package com.example.ever.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.ever.myapplication.sqlite.OperacionesBaseDatos;

import org.w3c.dom.Text;

public class detalle_pedido extends AppCompatActivity {

    ListView listaDetalle;
    Cursor c, client, venta;
    OperacionesBaseDatos datos;

    TableLayout tabla;
    TextView cliente, fecha, total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pedido);

        tabla = (TableLayout) findViewById(R.id.TableLayoutDetalle);
        cliente = (TextView) findViewById(R.id.textViewCliente2);
        fecha = (TextView) findViewById(R.id.textViewFecha);
        total = (TextView) findViewById(R.id.textViewTotal);

        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());
        Intent intent = getIntent();
        int codigoVenta = intent.getIntExtra("codigo", 0);

        venta = datos.obtenerVentasPorIdVenta(String.valueOf(codigoVenta));
        venta.moveToFirst();
        client = datos.obtenerClientesPorId(String.valueOf(venta.getInt(3)));
        client.moveToFirst();

        cliente.setText(client.getString(1) + " " + client.getString(2));
        fecha.setText(String.valueOf(venta.getString(1)));
        total.setText(String.valueOf(venta.getInt(2)) + " Gs.");

        c = datos.obtenerDetalleVentaPorIdVenta(String.valueOf(codigoVenta));
        int cantidadRegistros = c.getCount();
        String[] array = new String[cantidadRegistros];
        int i=0;
        if(c.moveToFirst()){
            do{
                Cursor prod = datos.obtenerProductosId(String.valueOf(c.getInt(3)));
                prod.moveToFirst();

                /*String linea =  "||Producto: " + prod.getString(1) + "\t||Precio: " + String.valueOf(prod.getInt(2)) + " Gs.||" + "\n||Cantidad: " + String.valueOf(c.getInt(4)) + "\t||Subtotal: " + String.valueOf(c.getInt(2) + " Gs.||");
                array[i] = linea;
                i++;*/


                TableRow row = new TableRow(this);
                TextView product = new TextView(this);
                product.setText(prod.getString(1));
                TextView precio = new TextView(this);
                precio.setText(String.valueOf(prod.getInt(2)) + " Gs.");
                TextView cantidad = new TextView(this);
                cantidad.setText(String.valueOf(c.getInt(4)));
                TextView subtotal = new TextView(this);
                subtotal.setText(String.valueOf(c.getInt(2)) + " Gs.");

                row.addView(product);
                row.addView(precio);
                row.addView(cantidad);
                row.addView(subtotal);

                tabla.addView(row);


            }while (c.moveToNext());
        }

        /*final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, array);
        listaDetalle = (ListView) findViewById(R.id.ListViewDetalles);
        listaDetalle.setAdapter(adapter);
        listaDetalle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/

    }
}
