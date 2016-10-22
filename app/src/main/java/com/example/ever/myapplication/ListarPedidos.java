package com.example.ever.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ever.myapplication.sqlite.OperacionesBaseDatos;

public class ListarPedidos extends AppCompatActivity implements View.OnClickListener{


    ListView listapedido;
    OperacionesBaseDatos datos;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_pedidos);

        listapedido = (ListView) findViewById(R.id.listViewPedidos);

        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());
        c= datos.obtenerVenta();
        int cantidadRegistros = c.getCount();
        int i = 0;
        String[] array = new String[cantidadRegistros];
        Integer[] arrayCodigo= new Integer[cantidadRegistros];
        if(c.moveToFirst()){
            do{
                String linea;
                if(c.getInt(0) < 10){
                    linea = "Pedido Nº: 000" + String.valueOf(c.getInt(0));
                }else if(c.getInt(0) > 10 && c.getInt(0) < 100){
                    linea = "Pedido Nº: 00" + String.valueOf(c.getInt(0));
                }else{
                    linea = "Pedido Nº: 0" + String.valueOf(c.getInt(0));
                }
                array [i]= linea;
                arrayCodigo[i]= c.getInt(0);
                i++;
            }while (c.moveToNext());

            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, array);
            final ArrayAdapter<Integer> adapterCodigo = new ArrayAdapter<Integer>(this, android.R.layout.simple_expandable_list_item_1, arrayCodigo);
            listapedido.setAdapter(adapter);
            listapedido.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(view.getContext(), detalle_pedido.class);
                    Integer codigo = adapterCodigo.getItem(position);
                    intent.putExtra("codigo", codigo);
                    startActivity(intent);

                }
            });
        }else{
            Toast.makeText(getApplicationContext(), "No se han realizado Pedidos", Toast.LENGTH_SHORT).show();
        }




    }

    @Override
    public void onClick(View v) {

        }
}

