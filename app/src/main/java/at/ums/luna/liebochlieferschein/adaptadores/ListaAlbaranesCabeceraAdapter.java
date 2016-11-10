package at.ums.luna.liebochlieferschein.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import at.ums.luna.liebochlieferschein.R;
import at.ums.luna.liebochlieferschein.modelos.CabeceraAlbaranes;

/**
 * Created by luna-aleixos on 31.05.2016.
 */
public class ListaAlbaranesCabeceraAdapter extends RecyclerView.Adapter<ListaAlbaranesCabeceraAdapter.ListaAlbaranesViewHolder>{

    private List<CabeceraAlbaranes> items;

    public static class ListaAlbaranesViewHolder extends RecyclerView.ViewHolder{
        //Campos de un item
        public TextView codigoAlbaran;
        public TextView nombre;
        public TextView fecha;

        public ListaAlbaranesViewHolder(View v){
            super(v);
            codigoAlbaran = (TextView) v.findViewById(R.id.idAlbaran);
            nombre = (TextView) v.findViewById(R.id.nombreCliente);
            fecha = (TextView) v.findViewById(R.id.fecha);
        }

    }

    public ListaAlbaranesCabeceraAdapter(List<CabeceraAlbaranes> items){
        this.items = items;
    }

    @Override
    public ListaAlbaranesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_cab_alb_card,parent,false);

        return  new ListaAlbaranesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListaAlbaranesViewHolder holder, int position) {
        holder.codigoAlbaran.setText(items.get(position).getCodigoAlbaran());
        holder.fecha.setText(String.valueOf(items.get(position).getFecha()));
        holder.nombre.setText(items.get(position).getNombreCliente());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
