package at.ums.luna.liebochlieferschein.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import at.ums.luna.liebochlieferschein.R;
import at.ums.luna.liebochlieferschein.modelos.Clientes;

/**
 * Created by luna-aleixos on 30.05.2016.
 */
public class ListaClientesAdapter extends RecyclerView.Adapter<ListaClientesAdapter.ListaClientesViewHolder>{

    private List<Clientes> items;

    public static class ListaClientesViewHolder extends RecyclerView.ViewHolder{

        public TextView idCliente;
        public TextView nombre;
        public TextView direccion;
        public TextView telefono;

        public ListaClientesViewHolder(View v){
            super(v);
            idCliente = (TextView) v.findViewById(R.id.idCliente);
            nombre = (TextView) v.findViewById(R.id.nombre);
            direccion = (TextView) v.findViewById(R.id.direccion);
            telefono = (TextView) v.findViewById(R.id.email);

        }

    }

    public ListaClientesAdapter(List<Clientes> items){
        this.items = items;

    }

    @Override
    public ListaClientesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_clientes_card, parent, false);


        return new ListaClientesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListaClientesViewHolder holder, int position) {
        holder.idCliente.setText(String.valueOf(items.get(position).getId()));
        holder.nombre.setText(items.get(position).getNombre());
        holder.direccion.setText(items.get(position).getDireccion());
        holder.telefono.setText(items.get(position).getTelefono());


    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
