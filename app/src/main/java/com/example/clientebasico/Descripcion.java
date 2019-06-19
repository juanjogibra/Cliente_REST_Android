package com.example.clientebasico;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class Descripcion extends AppCompatActivity {
     TextView nombre;
     TextView autor;
     TextView genero;
    ImageView imagen;
Bundle b;
Bundle bun;

    int pos;

   // String direccion, mensaje, asunto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.descripcion);


           b = getIntent().getExtras();
      //  Bundle b2 = getIntent().getExtras();
          nombre = findViewById(R.id.idNombreDetalles);
         autor = findViewById(R.id.idAutorDetalles);
         genero = findViewById(R.id.idGeneroDetalles);
         imagen = findViewById(R.id.idImageDetalles);

          libros lib = (libros) b.getSerializable("id1");

         int id = lib.getId();
         nombre.setText(lib.getNombre());
         autor.setText("Autor: "+lib.getAutor());
        genero.setText("Genero: "+lib.getGenero());
        //  imagen.setImageResource(lib.getFotoid());

        bun = new Bundle(); //Creamos dos bundles para transferir datos a la siguiente actividad
        bun = b;


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detalles_menus, menu);
      //  MenuItem itemnavegar = menu.findItem(R.id.navegar_id);
        return true;
    }

    public boolean onOptionsItemSelected (MenuItem item) {
        int option = item.getItemId();
        Bundle b = getIntent().getExtras();
        libros lib = (libros) b.getSerializable("id1");

        if(option == R.id.modificar_id) {
            Intent modificar = new Intent(Descripcion.this, Modificacion.class);
            //Bundle bu = new Bundle();
           // bu.putInt("pos", pos);
            modificar.putExtras(bun);
            startActivity(modificar);
            Toast.makeText(Descripcion.this,
                    "Modificar ", Toast.LENGTH_LONG).show();
        }
        if(option == R.id.borrar_id) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this); //Menu de confirmacion de eliminar

            //titulo
            builder.setTitle("Borrar elemento de la lista");

            //mensaje
            builder.setMessage("¿Esta seguro de que desea eliminarlo por completo?");

            //// permite cancelar el mensaje
            builder.setCancelable(true);

            //Accion del boton SI
            builder.setPositiveButton("Si",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            new TareaWSEliminar().execute();


                        }
                    });

            //Negative Button
            builder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Usually, negative use to close Dialog
                            //So, do nothing here, just dismiss it
                        }
                    });

            AlertDialog dialog = builder.create(); //Mostrar el dialogo de confirmacion.
            dialog.show();


        }

        return false;
    }




    private class TareaWSEliminar extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            Bundle bundle = getIntent().getExtras();
            libros lib = (libros) b.getSerializable("id1");

            int id = lib.getId();

            HttpClient httpClient = new DefaultHttpClient();


            HttpDelete del =
                    new HttpDelete("https://dam2.ieslamarisma.net/2019/juanjogonzalez/rest_slim_bd_libros/libro/" + id);

            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
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
                Toast.makeText(getApplicationContext(), "Libro eliminado correctamente", Toast.LENGTH_SHORT).show();

            }
        }
    }

}
