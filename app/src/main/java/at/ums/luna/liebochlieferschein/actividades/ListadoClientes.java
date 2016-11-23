package at.ums.luna.liebochlieferschein.actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.ArrayList;

import at.ums.luna.liebochlieferschein.R;
import at.ums.luna.liebochlieferschein.modelos.Clientes;

public class ListadoClientes extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private SearchView mSearchView;
    private ListView mListView;
    private ArrayList<Clientes> mClientesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_clientes);

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mListView = (ListView) findViewById(R.id.miListView);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void setupSearchView(){
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
        if(TextUtils.isEmpty(newText)){
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText.toString());
        }

        //clickEnLista;

        return true;
    }

//    private View obtenerListadoTareas(View view){
//        mClientesArrayList = new ArrayList<>();
//
//    }












}
