package at.ums.luna.liebochlieferschein.actividades;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import at.ums.luna.liebochlieferschein.R;
import at.ums.luna.liebochlieferschein.adaptadores.ListaClientesAdapter;
import at.ums.luna.liebochlieferschein.adaptadores.RecyclerItemClickListener;
import at.ums.luna.liebochlieferschein.modelos.Clientes;
import at.ums.luna.liebochlieferschein.servidor.Defaults;
import at.ums.luna.liebochlieferschein.servidor.MySingleton;
import at.ums.luna.liebochlieferschein.servidor.OperacionesServidor;

public class ListaClientes extends AppCompatActivity {

    /*
     Declarar instancias globales
      */
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    OperacionesServidor operacionesServidor;
    private List<Clientes> mClientes;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes);

        context = getApplicationContext();
        /*
        Codigo para obtenrer los datos de la DB
         */

        operacionesServidor = new OperacionesServidor();

        // Llamamos la taraea Async
        new ListClientAsync().execute();

    }

    public void initAdapter(){

        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        // Crear un nuevo adaptador

        adapter = new ListaClientesAdapter(mClientes);
        recycler.setAdapter(adapter);

        //Este metodo esta implementando la clase RecyclerItemClickListener que he creado
        recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Clientes clienteElegido = mClientes.get(position);

                        Toast.makeText(ListaClientes.this,"el Email es " + clienteElegido.getEmail().toString() , Toast.LENGTH_LONG).show();


                    }
                })
        );

    }

    private class ListClientAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            //obtenemos el listado de Clientes desde el servidor
            mClientes = operacionesServidor.verListaClientesServidor1(context);

//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
//                    Defaults.SERVER_URL + "obtener_clientes_1.php", (String) null,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//
//                            try {
//                                int count = 0;
//                                int total = response.getInt("total");
//                                Log.i("JJ","Hay " + total + " registros");
//
//                                JSONArray jsonArray = response.getJSONArray("clientes");
//                                while (count < total) {
//                                    JSONObject clientesObtenidos = jsonArray.getJSONObject(count);
//
//                                    Clientes cliente = new Clientes(clientesObtenidos.getInt("idCliente"),
//                                            clientesObtenidos.getString("nombre"),
//                                            clientesObtenidos.getString("direccion"),
//                                            clientesObtenidos.getString("telefono"),
//                                            clientesObtenidos.getString("email"));
//                                    mClientes.add(cliente);
//                                    Log.i("JJ", String.valueOf(clientesObtenidos.getInt("idCliente")));
//
//                                    count++;
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                                Log.i("JJ",e.toString());
//                            }
//
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                }
//            });
//
//            MySingleton.getInstance(context).addToRequestque(jsonObjectRequest);
//            Log.i("JJ", "total " + String.valueOf(mClientes.size()));



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //inicializamos el RecyclerView y el adapter con la data obtenida
            initAdapter();
        }
    }
}
