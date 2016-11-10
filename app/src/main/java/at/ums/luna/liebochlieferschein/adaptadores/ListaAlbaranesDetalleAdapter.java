package at.ums.luna.liebochlieferschein.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import at.ums.luna.liebochlieferschein.R;
import at.ums.luna.liebochlieferschein.modelos.DetalleAlbaranes;

/**
 * Created by luna-aleixos on 08.06.2016.
 */
public class ListaAlbaranesDetalleAdapter extends RecyclerView.Adapter<ListaAlbaranesDetalleAdapter.ListaAlbaranesViewHolder>{

    private List<DetalleAlbaranes> items;

    public static class ListaAlbaranesViewHolder extends RecyclerView.ViewHolder{

        //Campos de un item
        public TextView detalle;
        public TextView linea;
        public TextView cantidad;
        public TextView tipo;


        public ListaAlbaranesViewHolder(View v){
            super(v);

            linea = (TextView) v.findViewById(R.id.numeroLinea);
            detalle = (TextView) v.findViewById(R.id.descripcion);
            cantidad = (TextView) v.findViewById(R.id.cantidad);
            tipo = (TextView) v.findViewById(R.id.tipo);
        }

    }

    public ListaAlbaranesDetalleAdapter(List<DetalleAlbaranes> items){
        this.items = items;
    }

    @Override
    public ListaAlbaranesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_det_alb_card,parent,false);

        return  new ListaAlbaranesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListaAlbaranesViewHolder holder, int position) {
        holder.linea.setText(String.valueOf(items.get(position).getLinea()));
        holder.detalle.setText(items.get(position).getDetalle());
        holder.cantidad.setText(String.valueOf(items.get(position).getCantidad()));
        holder.tipo.setText(items.get(position).getTipo());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

