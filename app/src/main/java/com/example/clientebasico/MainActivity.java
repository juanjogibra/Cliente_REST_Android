package com.example.clientebasico;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<libros> obras;
    RecyclerView rv;
    AdapterDatos adaptador;
    SwipeRefreshLayout mSwipeRefreshLayout;

    int pos = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AsyncTaskRunner runner = new AsyncTaskRunner(); //Objeto de clase Asynctask (Ralentizar programa)
        String sleepTime = "3"; //Tiempo de espera
        runner.execute(sleepTime); //Ejecutar asynctask con el tiempo de espera determinado


        FloatingActionButton fab = findViewById(R.id.btnAnadir);

// Acción del boton de añadir
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nuevo = new Intent(MainActivity.this, nuevo.class);

                startActivity(nuevo);
            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Snackbar.make(v, "Registra un nuevo libro", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return false;
            }
        });



        rv = findViewById(R.id.recyclerId);
        obras = new ArrayList<>();
        rv.setLayoutManager(new LinearLayoutManager(this));
        // llenarLista();
       // obras.add(new libros("libro", "autor"));
        adaptador = new AdapterDatos(obras);
       // rv.setAdapter(adaptador);
       // adaptador.notifyDataSetChanged();

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Accion de un elemento del recyclerview
                Intent abrirDellates = new Intent(MainActivity.this, Descripcion.class);
                libros l = obras.get(rv.getChildAdapterPosition(v));
                pos = rv.getChildAdapterPosition(v);
                int ide = l.getId();
                Bundle b = new Bundle(); //Creamos dos bundles para transferir datos a la siguiente actividad
                // Bundle b2 = new Bundle();
                b.putSerializable("id1", l);
                // b2.putInt("pos", ide);
                abrirDellates.putExtras(b);
                //abrirDellates.putExtras(b2);
                startActivity(abrirDellates);
                Toast.makeText(MainActivity.this,
                        "ID: " + ide + "", Toast.LENGTH_LONG).show();
            }
        });

    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //Menu superior donde insertamos la barra de busqueda
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        MenuItem itemsearch = menu.findItem(R.id.search_id);
        SearchView sv = (SearchView) itemsearch.getActionView(); //Widget de busqueda en la interfaz
        sv.setQueryHint("buscar libro...");
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) { //metodo del adapter que filtra el contenido
                adaptador.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }
*/

    public void llenarLista() {


        /*


        Uri librosUri = LibrosProvider.CONTENT_URI;
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(librosUri,
                projection, //Columnas a devolver
                null,       //Condición de la query
                null,       //Argumentos variables de la query
                null);      //Orden de los resultados

        if (cur.moveToFirst()) {
            String nombre;
            String autor;
            String genero;
            String foto;
            int valor;

            int colNombre = cur.getColumnIndex(LibrosProvider.Libros.COL_NOMBRE);
            int colAutor = cur.getColumnIndex(LibrosProvider.Libros.COL_AUTOR);
            int colGenero = cur.getColumnIndex(LibrosProvider.Libros.COL_GENERO);
            int colFoto = cur.getColumnIndex(LibrosProvider.Libros.COL_FOTO);

            //txtResultados.setText("");

            do {
                nombre = cur.getString(colNombre);
                autor = cur.getString(colAutor);
                genero = cur.getString(colGenero);
                foto = cur.getString(colFoto);
                valor = getResources().getIdentifier(foto, "drawable", getPackageName());
                obras.add(new libros(nombre, autor, genero, valor));
                // txtResultados.append(nombre + " - " + autor + " - " + genero + "\n");

            } while (cur.moveToNext());
        }*/
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        // private String resp;
        ProgressDialog progressDialog;
        //private String[] libros;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                int time = Integer.parseInt(params[0]) * 1000;

                Thread.sleep(time);

            } catch (InterruptedException e) {
                e.printStackTrace();
                //  resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                // resp = e.getMessage();
            }

            boolean resul = true;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet del =
                    new HttpGet("https://dam2.ieslamarisma.net/2019/juanjogonzalez/rest_slim_bd_libros/libros");

            del.setHeader("content-type", "application/json");
            /*
             */
            try {
                System.out.println();
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);
                //   libros = new String[respJSON.length()];
                for (int i = 0; i < respJSON.length(); i++) {
                    JSONObject obj = respJSON.getJSONObject(i);
                    libros libros = new libros();
                    libros.setId(obj.getInt("id"));
                    libros.setNombre(obj.getString("nombre"));
                    libros.setAutor(obj.getString("autor"));
                    libros.setGenero(obj.getString("genero"));
                    libros.setDescripcion(obj.getString("descripcion"));
                    obras.add(libros);
                    // libros.setFotoid(obj.getString("fotoid"));

                    // int valor = getResources().getIdentifier(idfoto, "drawable", getPackageName());

                }

            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return null;

            /*publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                int time = Integer.parseInt(params[0]) * 1000;

                Thread.sleep(time);
                //  resp = "Slept for " + params[0] + " seconds";
            } catch (InterruptedException e) {
                e.printStackTrace();
                //  resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                // resp = e.getMessage();
            }*/

        }
        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            rv.setAdapter(adaptador);
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Espere por favor",
                    "Cargando la base de datos. Puede tardar un rato");
        }


        protected void onProgressUpdate(String... text) {
            // finalResult.setText(text[0]);

        }
    }


}
