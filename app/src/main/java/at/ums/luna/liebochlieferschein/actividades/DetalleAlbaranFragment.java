package at.ums.luna.liebochlieferschein.actividades;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import at.ums.luna.liebochlieferschein.R;
import at.ums.luna.liebochlieferschein.adaptadores.ListaAlbaranesDetalleAdapter;
import at.ums.luna.liebochlieferschein.adaptadores.RecyclerItemClickListener;
import at.ums.luna.liebochlieferschein.database.OperacionesBaseDatos;
import at.ums.luna.liebochlieferschein.modelos.DetalleAlbaranes;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetalleAlbaranFragment extends Fragment {


    /*
     Declarar instancias globales
      */
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;


    private String codigoAlbaranObtenido;
    private OperacionesBaseDatos mOperacionesBaseDatos;
    private Context esteContexto;

    private List<DetalleAlbaranes> mDetalleAlbaranes;
    private String valorCodigoAlbaran;
    private String valorLineaAlbaran;

    private TextView ultimaLinea;
    private int valorUltimaLinea;

    public DetalleAlbaranFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        esteContexto = container.getContext();

        Bundle args = getArguments();
        codigoAlbaranObtenido = args.getString("codigoObtenido");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detalle_albaran, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        cargaInicial();

        //Codigo para el onClickListener
        getView().findViewById(R.id.botonNuevaLinea).setOnClickListener(mGlobal_onClickListener);
    }

    //Intents para cualquier botón de la actividad
    final View.OnClickListener mGlobal_onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.botonNuevaLinea:
                    nuevaLinea();
                    break;
            }
        }
    };


    private void cargaInicial() {
        mOperacionesBaseDatos = new OperacionesBaseDatos(esteContexto);



        ultimaLinea = (TextView) getView().findViewById(R.id.tvNumeroLineas);




        // Obtener el Recycler
        recycler = (RecyclerView) getView().findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(esteContexto);
        recycler.setLayoutManager(lManager);


        //Este metodo esta implementando la clase RecyclerItemClickListener que he creado
        recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(esteContexto, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        DetalleAlbaranes detalleElegido = mDetalleAlbaranes.get(position);

                        valorCodigoAlbaran = detalleElegido.getCodigoAlbaran();
                        valorLineaAlbaran = String.valueOf(detalleElegido.getLinea());

                        abrirDetalle(valorCodigoAlbaran,valorLineaAlbaran);
//                        Intent intento = new Intent(esteContexto, FormularioAlbaranesDetalle.class);
//                        intento.putExtra("codigoAlbaran", valorCodigoAlbaran);
//                        intento.putExtra("linea", valorLineaAlbaran);
//                        startActivity(intento);

                    }
                })
        );


    }



    @Override
    public void onResume() {
        super.onResume();

        valorUltimaLinea = mOperacionesBaseDatos.ultimaLineaAlbaran(codigoAlbaranObtenido);
        ultimaLinea.setText(String.valueOf(valorUltimaLinea));


        mDetalleAlbaranes = mOperacionesBaseDatos.verListaDetalleAlbaran(codigoAlbaranObtenido);
        adapter = new ListaAlbaranesDetalleAdapter(mDetalleAlbaranes);
        recycler.setAdapter(adapter);

    }

    public void abrirDetalle(String codigo, String linea){

        Intent intento = new Intent(esteContexto, FormularioAlbaranesDetalle.class);
        intento.putExtra("codigoAlbaran", codigo);
        intento.putExtra("linea", linea);
        startActivity(intento);

    }

    public void nuevaLinea(){
        int nuevaLinea = valorUltimaLinea+1;
        String nuevaLineaTexto = String.valueOf(nuevaLinea);

        mOperacionesBaseDatos = new OperacionesBaseDatos(esteContexto);
        mOperacionesBaseDatos.nuevoDetalleAlbaran(nuevaLinea,codigoAlbaranObtenido);

        valorUltimaLinea = mOperacionesBaseDatos.ultimaLineaAlbaran(codigoAlbaranObtenido);
        ultimaLinea.setText(String.valueOf(valorUltimaLinea));

        abrirDetalle(codigoAlbaranObtenido,nuevaLineaTexto);

    }


}
