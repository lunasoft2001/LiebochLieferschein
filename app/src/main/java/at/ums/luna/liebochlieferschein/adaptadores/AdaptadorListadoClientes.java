package at.ums.luna.liebochlieferschein.adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import at.ums.luna.liebochlieferschein.R;
import at.ums.luna.liebochlieferschein.modelos.Clientes;

/**
 * Created by luna-aleixos on 23.11.2016.
 */

public class AdaptadorListadoClientes extends BaseAdapter implements Filterable{

    public Context context;
    public ArrayList<Clientes> objetoArrayList;
    public ArrayList<Clientes> origen;

    public AdaptadorListadoClientes(Context context, ArrayList<Clientes> objetoArrayList){
        super();
        this.context = context;
        this.objetoArrayList = objetoArrayList;
    }

    public class ObjetoHolder{
        TextView tvIdCliente;
        TextView tvNombre;
    }


    @Override
    public int getCount() {
        return objetoArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return objetoArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ObjetoHolder holder;
        if(convertView == null){
            convertView= LayoutInflater.from(context).inflate(R.layout.fila_listado_clientes, parent,false);
            holder = new ObjetoHolder();
            holder.tvIdCliente = (TextView) convertView.findViewById(R.id.tvIdCliente);
            holder.tvNombre = (TextView) convertView.findViewById(R.id.nombre);
            convertView.setTag(holder);
        }
        else{
            holder=(ObjetoHolder)convertView.getTag();
        }

        holder.tvIdCliente.setText(Integer.toString(objetoArrayList.get(position).getId()));
        holder.tvNombre.setText(objetoArrayList.get(position).getNombre());


        return convertView;

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Clientes> resultados = new ArrayList<>();
                if (origen == null)
                    origen = objetoArrayList;
                if(constraint != null){
                    if(origen != null & origen.size() >0) {
                        for(final Clientes g : origen) {
                            if(g.getNombre().toLowerCase().contains(constraint.toString()))
                                resultados.add(g);
                        }
                        for(final Clientes g : origen) {
                            if(String.valueOf(g.getId()).contains(constraint.toString()))
                                resultados.add(g);
                        }

                    }



                    oReturn.values = resultados;
                }

                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                objetoArrayList = (ArrayList<Clientes>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}