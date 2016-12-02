package at.ums.luna.liebochlieferschein.actividades;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import at.ums.luna.liebochlieferschein.R;
import at.ums.luna.liebochlieferschein.adaptadores.AdaptadorListadoClientes;
import at.ums.luna.liebochlieferschein.modelos.Clientes;
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

        @Override
        protected Void doInBackground(Void... params) {
            //obtenemos el listado de Clientes desde el servidor

            mOperacionesServidor = new OperacionesServidor();
            mClientesArrayList = mOperacionesServidor.verListaClientesServidor1(ListadoClientes.this);

            while (mClientesArrayList.size()==0){}

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
