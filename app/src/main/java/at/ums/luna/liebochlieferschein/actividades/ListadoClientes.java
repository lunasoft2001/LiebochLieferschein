package at.ums.luna.liebochlieferschein.actividades;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SearchView;

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
import at.ums.luna.liebochlieferschein.adaptadores.AdaptadorListadoClientes;
import at.ums.luna.liebochlieferschein.modelos.Clientes;
import at.ums.luna.liebochlieferschein.servidor.Defaults;
import at.ums.luna.liebochlieferschein.servidor.MySingleton;
import at.ums.luna.liebochlieferschein.servidor.OperacionesServidor;

public class ListadoClientes extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private SearchView mSearchView;
    private ListView mListView;
    private ArrayList<Clientes> mClientesArrayList;
    private OperacionesServidor mOperacionesServidor;
    private AdaptadorListadoClientes mAdaptadorListadoClientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_clientes);

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mListView = (ListView) findViewById(R.id.miListView);
        mClientesArrayList = new ArrayList<>();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        new obtenerListado().execute();

    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint("Suchen...");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText.toString());
        }

        return true;
    }


    private class obtenerListado extends AsyncTask<Void, Void, Void> {

        int acabado = 0;

        @Override
        protected Void doInBackground(Void... params) {
            //obtenemos el listado de Clientes desde el servidor
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    Defaults.SERVER_URL + "obtener_clientes_1.php", (String) null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                int count = 0;
                                int total = response.getInt("total");

                                JSONArray jsonArray = response.getJSONArray("clientes");

                                while (count < total) {
                                    JSONObject clientesObtenidos = jsonArray.getJSONObject(count);

                                    Clientes cliente = new Clientes(clientesObtenidos.getInt("idCliente"),
                                            clientesObtenidos.getString("nombre"),
                                            clientesObtenidos.getString("direccion"),
                                            clientesObtenidos.getString("telefono"),
                                            clientesObtenidos.getString("email"));
                                    mClientesArrayList.add(cliente);

                                    count++;
                                }

                                if (count == total){acabado =1;}

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i("JJ", e.toString());
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            MySingleton.getInstance(ListadoClientes.this).addToRequestque(jsonObjectRequest);

            int espera = 1;
            while (acabado == 0){

                espera++;
            }

            Log.i("JJ", String.valueOf(espera));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            adaptador();

            setupSearchView();

        }
    }


    private void adaptador(){
        mAdaptadorListadoClientes = new AdaptadorListadoClientes(this, mClientesArrayList);
        mListView.setAdapter(mAdaptadorListadoClientes);
        mListView.setTextFilterEnabled(true);

    }

}
