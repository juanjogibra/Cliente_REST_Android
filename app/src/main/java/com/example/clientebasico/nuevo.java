package com.example.clientebasico;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class nuevo extends AppCompatActivity {
    //final int PICK_IMAGE = 1;

    SQLiteDatabase db;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo);


        nombre = findViewById(R.id.etnombre);
        autor = findViewById(R.id.etautor);
        descripcion = findViewById(R.id.etdesc);
        sp = findViewById(R.id.spinner);

        tw = findViewById(R.id.textView);
        ib = (ImageButton) findViewById(R.id.imageButton);
        ib.setEnabled(false);

        /*Insertar ins = new Insertar();
        ins.execute(nombre.getText().toString(), autor.getText().toString(), sp.getSelectedItem().toString(), descripcion.getText().toString());
        */

        /*ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });*/
        FloatingActionButton fab = findViewById(R.id.btnGuardar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Insertar ins = new Insertar();
                ins.execute(nombre.getText().toString(), autor.getText().toString(), sp.getSelectedItem().toString(), descripcion.getText().toString());

               // onBackPressed();

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




private class Insertar extends AsyncTask<String, Integer, Boolean> {

    protected Boolean doInBackground(String... params) {

        boolean resultado = true;

        HttpClient httpClient = new DefaultHttpClient();
        //URL del servidor donde insertara los datos
        HttpPost post = new HttpPost("https://dam2.ieslamarisma.net/2019/juanjogonzalez/rest_slim_bd_libros/libro");
                post.setHeader("content-type", "application/json");

        try {
            //Construimos el objeto cliente en formato JSON
            JSONObject datos = new JSONObject();

            //Datos a insertar(es necesario insertarlos todos)
            datos.put("nombre", params[0]);
            datos.put("autor", params[1]);
            datos.put("genero", params[2]);
            datos.put("descripcion", params[3]);
            datos.put("favorito", 0);
            datos.put("idfoto", "defecto");
            datos.put("url", "www.google.es");

            StringEntity entity = new StringEntity(datos.toString());
            post.setEntity(entity);

            HttpResponse resp = httpClient.execute(post);
            String respStr = EntityUtils.toString(resp.getEntity());

           /* if (!respStr.equals("true"))
                resultado = false;*/
        } catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
            resultado = false;
        }

        return resultado;
    }

    protected void onPostExecute(Boolean result) {

        if (result) {
            Toast.makeText(nuevo.this,
                    "Se ha insertado un nuevo valor", Toast.LENGTH_LONG).show();
        }
    }
}
}