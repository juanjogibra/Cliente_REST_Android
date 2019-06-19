package com.example.clientebasico;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos>
implements View.OnClickListener, Filterable  {

    ArrayList<libros> obras;
    ArrayList<libros> obrastotales;
    private View.OnClickListener mlistener;

    public AdapterDatos(ArrayList<libros> obras) {

        this.obras = obras;
        obrastotales = new ArrayList<>(obras);
    }

    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null, false);
        view.setOnClickListener(this);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderDatos holder, int position) {
        /*String s;
        s= items.getString(consultas.nombre);
        holder.elnombre.setText(s);
        s= items.getString(consultas.autor);
        holder.elnombre.setText(s);
*/


        holder.elnombre.setText(obras.get(position).getNombre());
        holder.elautor.setText(obras.get(position).getAutor());
        //holder.laimagen.setImageResource(obras.get(position).getFotoid());

    }

    @Override
    public int getItemCount() {
        return obras.size();
    }


    public void setOnClickListener(View.OnClickListener mlistener) {

        this.mlistener = mlistener;
    }

    @Override
    public void onClick(View v) {
        if (mlistener != null) {
            mlistener.onClick(v);

        }
    }

    @Override
    public Filter getFilter() {
        return examplefilter;
    }
    private Filter examplefilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
           ArrayList<libros> listafiltrada = new ArrayList<>();
           if (constraint==null || constraint.length()==0) {
               listafiltrada.addAll(obrastotales);
           } else {
               String filterpattern = constraint.toString().toLowerCase().trim();
               for (libros hard: obrastotales) {
                   if(hard.getNombre().toLowerCase().contains(filterpattern)) {
                       listafiltrada.add(hard);
                   }
               }
           }
           FilterResults resultados = new FilterResults();
           resultados.values = listafiltrada;
            return resultados;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
obras.clear();
obras.addAll((ArrayList)results.values);
notifyDataSetChanged();
        }
    };

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView elnombre;
        ImageView laimagen;
        TextView elautor;

        public ViewHolderDatos(View itemView) {

            super(itemView);
            elnombre = itemView.findViewById(R.id.idNombre);
            elautor = itemView.findViewById(R.id.idAutor);
            laimagen = itemView.findViewById(R.id.idImage);

        }
    }

  /*  interface consultas {
        int id = 1;
        int nombre = 2;
        int autor = 3;
        int genero = 4;
        int descripcion = 5;
        int favorito = 6;
        int idfoto =7;
    }*/
}


