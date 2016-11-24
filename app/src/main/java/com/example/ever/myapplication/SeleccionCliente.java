package com.example.ever.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.ever.myapplication.sqlite.BaseDatosPedidos;
import com.example.ever.myapplication.sqlite.OperacionesBaseDatos;

public class SeleccionCliente extends AppCompatActivity implements View.OnClickListener{

    OperacionesBaseDatos datos;
    Cursor c;
    //CursorAdap cursorAdap;



    ListView listaClientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_cliente);

        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());
        c= datos.obtenerClientes();
        int cantidadRegistros = c.getCount();
        int i = 0;
        String[] array= new String [cantidadRegistros];
        Integer[] arrayCodigo= new Integer [cantidadRegistros];
        if(c.moveToFirst()) {
            do {
                String linea = c.getString(1) + " " + c.getString(2);
                Integer lineaCodigo = c.getInt(0);
                array[i] = linea;
                arrayCodigo[i] = lineaCodigo;
                i++;
            } while (c.moveToNext());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, array);
        final ArrayAdapter<Integer> adapterCodigo = new ArrayAdapter<Integer>(this,android.R.layout.simple_expandable_list_item_1, arrayCodigo);
        listaClientes = (ListView) findViewById(R.id.ListViewClientes);
        listaClientes.setAdapter(adapter);
        listaClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), LevantamientoPedido.class);
                Integer codigo = position +1;
                intent.putExtra("codigo", codigo);
                String cliente = adapter.getItem(position);
                intent.putExtra("cliente", cliente);
                startActivity(intent);
            }
        });

    }



    @Override
    public void onClick(View v) {


    }


}
