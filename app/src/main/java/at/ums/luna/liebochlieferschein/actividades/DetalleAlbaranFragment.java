package at.ums.luna.liebochlieferschein.actividades;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import at.ums.luna.liebochlieferschein.R;
import at.ums.luna.liebochlieferschein.adaptadores.ListaAlbaranesDetalleAdapter;
import at.ums.luna.liebochlieferschein.adaptadores.RecyclerItemClickListener;
import at.ums.luna.liebochlieferschein.database.OperacionesBaseDatos;
import at.ums.luna.liebochlieferschein.modelos.DetalleAlbaranes;
import at.ums.luna.liebochlieferschein.servidor.Defaults;
import at.ums.luna.liebochlieferschein.servidor.MySingleton;
import at.ums.luna.liebochlieferschein.servidor.OperacionesServidor;

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
//    private OperacionesBaseDatos mOperacionesBaseDatos;
    private OperacionesServidor mOperacionesServidor;
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
//        mOperacionesBaseDatos = new OperacionesBaseDatos(esteContexto);
        mOperacionesServidor = new OperacionesServidor();


        obtenerUltimaLinea();

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


                    }
                })
        );


    }



    @Override
    public void onResume() {
        super.onResume();

//        valorUltimaLinea = mOperacionesBaseDatos.ultimaLineaAlbaran(codigoAlbaranObtenido);
//        ultimaLinea.setText(String.valueOf(valorUltimaLinea));
        valorUltimaLinea = 0;
        obtenerUltimaLinea();

        new ListaDetalleAsync().execute();
//        mDetalleAlbaranes = mOperacionesBaseDatos.verListaDetalleAlbaran(codigoAlbaranObtenido);
//        adapter = new ListaAlbaranesDetalleAdapter(mDetalleAlbaranes);
//        recycler.setAdapter(adapter);

    }

    private class ListaDetalleAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            //obtenemos el listado de Clientes desde el servidor
            mDetalleAlbaranes = mOperacionesServidor.verListaDetalleAlbaran(esteContexto, codigoAlbaranObtenido);

            while ( mDetalleAlbaranes.size()==0){}

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //verificamos que no esta vacio
            if (mDetalleAlbaranes.get(0).getCodigoAlbaran() != "error") {

                //inicializamos el RecyclerView y el adapter con la data obtenida
                adapter = new ListaAlbaranesDetalleAdapter(mDetalleAlbaranes);
                recycler.setAdapter(adapter);
            }
        }
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

//        mOperacionesBaseDatos = new OperacionesBaseDatos(esteContexto);
//        mOperacionesBaseDatos.nuevoDetalleAlbaran(nuevaLinea,codigoAlbaranObtenido);
        mOperacionesServidor = new OperacionesServidor();
        mOperacionesServidor.nuevoDetalleAlbaran(esteContexto,valorUltimaLinea,codigoAlbaranObtenido);


        obtenerUltimaLinea();


        abrirDetalle(codigoAlbaranObtenido,nuevaLineaTexto);

    }

    private void obtenerUltimaLinea(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Defaults.SERVER_URL + "obtener_ultima_linea_detalle.php?codigoAlbaran=" + codigoAlbaranObtenido,
                (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    valorUltimaLinea = response.getInt("Max(linea)");
                    ultimaLinea = (TextView) getView().findViewById(R.id.tvNumeroLineas);
                    ultimaLinea.setText(String.valueOf(valorUltimaLinea));

                }catch (JSONException e){
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(esteContexto, "Algo salio mal " + error,Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        });

        MySingleton.getInstance(esteContexto).addToRequestque(jsonObjectRequest);
    }


}
