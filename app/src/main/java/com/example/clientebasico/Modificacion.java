package com.example.clientebasico;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class Modificacion extends AppCompatActivity {



    ImageButton ib;
    TextView tw;
    EditText nombre;
    EditText autor;
    Spinner sp;
    EditText descripcion;

    String name;
    String author;
    String gender;
    String description;

    Bundle bun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo);

        // Bundle bu = getIntent().getExtras();
         //final int pos = bu.getInt("pos");

        bun = getIntent().getExtras();

        Bundle b = getIntent().getExtras();
        libros lib = (libros) bun.getSerializable("id1");
        Bundle b2 = getIntent().getExtras();


        nombre = findViewById(R.id.etnombre);
        autor = findViewById(R.id.etautor);
        descripcion = findViewById(R.id.etdesc);
        sp = findViewById(R.id.spinner);

      nombre.setText(lib.getNombre());
      autor.setText(lib.getAutor());
     descripcion.setText(lib.getDescripcion());

        tw = findViewById(R.id.textView);

        FloatingActionButton fab = findViewById(R.id.btnGuardar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new TareaWSActualizar().execute(nombre.getText().toString(),autor.getText().toString()
                        ,sp.getSelectedItem().toString(),descripcion.getText().toString());

            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Snackbar.make(v, "Registra el nuevo libro creado", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return false;
            }
        });


    }
    private class TareaWSActualizar extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {
            bun = getIntent().getExtras();

            libros lib = (libros) bun.getSerializable("id1");
            int id = lib.getId();

            boolean resul = true;
            Bundle bundle = getIntent().getExtras();


            HttpClient httpClient = new DefaultHttpClient();


            HttpPut put = new HttpPut("https://dam2.ieslamarisma.net/2019/juanjogonzalez/rest_slim_bd_libros/libro/" +id);
            put.setHeader("content-type", "application/json");

            try {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();
                dato.put("nombre", params[0]);
                dato.put("autor", params[1]);
                dato.put("genero", params[2]);
                dato.put("descripcion", params[3]);
                dato.put("favorito", 0);
                dato.put("idfoto","defecto");
                dato.put("url", "www.google.es");


                StringEntity entity = new StringEntity(dato.toString());
                put.setEntity(entity);

                HttpResponse resp = httpClient.execute(put);
                String respStr = EntityUtils.toString(resp.getEntity());

               /* if (!respStr.equals("true"))
                    resul = false;*/
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                Toast.makeText(getApplicationContext(), "Libro modificado correctamente", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
