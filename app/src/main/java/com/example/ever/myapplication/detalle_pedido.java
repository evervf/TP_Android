package com.example.ever.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.icu.text.NumberFormat;
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

    Cursor c, client, venta;
    OperacionesBaseDatos datos;

    TableLayout tabla;
    TextView cliente, fecha, total, direcc, telefono, v;

    NumberFormat formateo = NumberFormat.getIntegerInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pedido);

        tabla = (TableLayout) findViewById(R.id.TableLayoutDetalle);
        cliente = (TextView) findViewById(R.id.textViewCliente2);
        fecha = (TextView) findViewById(R.id.textViewFecha);
        total = (TextView) findViewById(R.id.textViewTotal);
        telefono = (TextView) findViewById(R.id.textViewTel);
        direcc = (TextView) findViewById(R.id.textViewDir);
        v = (TextView) findViewById(R.id.textViewVenta);

        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());
        Intent intent = getIntent();
        int codigoVenta = intent.getIntExtra("codigo", 0);

        venta = datos.obtenerVentasPorIdVenta(String.valueOf(codigoVenta));
        venta.moveToFirst();
        client = datos.obtenerClientesPorId(String.valueOf(venta.getInt(3)));
        client.moveToFirst();

        cliente.setText(client.getString(1) + " " + client.getString(2));
        direcc.setText(client.getString(4));
        telefono.setText(client.getString(5));
        fecha.setText(String.valueOf(venta.getString(1)));
        total.setText(String.valueOf(formateo.format(venta.getInt(2))) + " Gs.");

        int pedido;
        c = datos.obtenerDetalleVentaPorIdVenta(String.valueOf(codigoVenta));
        if(c.moveToFirst()){

            pedido = c.getInt(1);
            if(pedido < 10){
                v.setText(String.valueOf("000" + pedido));
            }else if(pedido >= 10 && pedido < 100){
                v.setText(String.valueOf("00" + pedido));
            }else{
                v.setText(String.valueOf("0" + pedido));
            }

            do{
                Cursor prod = datos.obtenerProductosId(String.valueOf(c.getInt(3)));
                prod.moveToFirst();

                TableRow row = new TableRow(this);
                TextView product = new TextView(this);
                product.setText(prod.getString(1));
                TextView precio = new TextView(this);
                precio.setText(String.valueOf(formateo.format(prod.getInt(2))) + " Gs.");
                TextView cantidad = new TextView(this);
                cantidad.setText(String.valueOf(c.getInt(4)));
                TextView subtotal = new TextView(this);
                subtotal.setText(String.valueOf(formateo.format(c.getInt(2))) + " Gs.");

                row.addView(product);
                row.addView(precio);
                row.addView(cantidad);
                row.addView(subtotal);

                tabla.addView(row);


            }while (c.moveToNext());
        }

    }
}
