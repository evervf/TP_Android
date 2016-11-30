package com.example.ever.myapplication;

import android.content.Intent;
import android.database.DatabaseUtils;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ever.myapplication.modelo.Cliente;
import com.example.ever.myapplication.modelo.Producto;
//import com.example.ever.myapplication.modelo.TipoProducto;
import com.example.ever.myapplication.modelo.Vendedor;
import com.example.ever.myapplication.sqlite.OperacionesBaseDatos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button botonNuevo;
    Button botonListar;

    OperacionesBaseDatos datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getApplicationContext().deleteDatabase("pedidos.db");
        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());

        botonNuevo= (Button) findViewById(R.id.ButtonNuevo);
        botonNuevo.setOnClickListener(this);

        botonListar= (Button) findViewById(R.id.ButtonListar);
        botonListar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.ButtonNuevo:
                Intent intent= new Intent(this, SeleccionCliente.class);
                startActivity(intent);
                break;
            case R.id.ButtonListar:
                Intent intent2= new Intent(this, ListarPedidos.class);
                startActivity(intent2);
                break;
            default:
                break;
        }

    }
}

