package at.ums.luna.liebochlieferschein.actividades;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import at.ums.luna.liebochlieferschein.R;
import at.ums.luna.liebochlieferschein.adaptadores.ListaAlbaranesCabeceraAdapter;
import at.ums.luna.liebochlieferschein.adaptadores.RecyclerItemClickListener;
import at.ums.luna.liebochlieferschein.database.OperacionesBaseDatos;
import at.ums.luna.liebochlieferschein.modelos.CabeceraAlbaranes;
import at.ums.luna.liebochlieferschein.servidor.OperacionesServidor;

public class ListaAlbaranesCabecera extends AppCompatActivity {

    /*
  Declarar instancias globales
   */
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private Toolbar toolbar;

    /*
    Variables SOLO para SQLite
     */
    OperacionesBaseDatos mOperacionesBaseDatos;
    private List<CabeceraAlbaranes> mCabeceraAlbaranes;
    OperacionesServidor operacionesServidor;
    Context context;



    /*
    Variables diversas
     */
    String idTrabajador;
    int ultimoAlbaran;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_albaranes_cabecera);

        context= getApplicationContext();


        Intent intento = getIntent();
        Bundle bundle = intento.getExtras();
        if (bundle != null){
            idTrabajador = bundle.getString("idTrabajador");
        }

        //activamos la toolbar
        setToolbar();


        /*
        Codigo para el floating button
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.botonNuevaCabeceraAlbaran);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Comprobamos si existe albaran previo
                if (ultimoAlbaran == 0){
                    crearNuevaTemporadaAlbaran();


                }else {

                    mOperacionesBaseDatos.nuevaCabeceraAlbaran(ultimoAlbaran, idTrabajador);

                    int nuevoAlbaran = ultimoAlbaran + 1;
                    String nuevoCodigoAlbaran = idTrabajador + String.valueOf(nuevoAlbaran);

                    Intent intento = new Intent(ListaAlbaranesCabecera.this, FormularioAlbaranesCabecera.class);
                    intento.putExtra("codigoAlbaran", nuevoCodigoAlbaran);
                    startActivity(intento);
                }
            }
        });


        mOperacionesBaseDatos = new OperacionesBaseDatos(this);
        operacionesServidor = new OperacionesServidor();

        new ListaCabeceraAsync().execute();

    }

    public void initAdapter(){
        /*
        Obtener el Recycler
         */

        recycler = (RecyclerView) findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);

        /*
        Usar un administrador para LinearLayout
         */
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        adapter = new ListaAlbaranesCabeceraAdapter(mCabeceraAlbaranes);
        recycler.setAdapter(adapter);


        //Este metodo esta implementando la clase RecyclerItemClickListener que he creado
        recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        CabeceraAlbaranes cabeceraActual = mCabeceraAlbaranes.get(position);
                        Intent intento = new Intent(ListaAlbaranesCabecera.this,FormularioAlbaranesCabecera.class);
                        intento.putExtra("codigoAlbaran",cabeceraActual.getCodigoAlbaran());
                        startActivity(intento);

                    }
                })
        );


    }

    private void setToolbar() {
        //añadir la Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.lista_albaranes));
//        toolbar.setLogo(R.mipmap.ic_launcher);
    }

    private class ListaCabeceraAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            //obtenemos el listado de Clientes desde el servidor
            mCabeceraAlbaranes = operacionesServidor.verListaAlbaranesCabeceraCompleta(context);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //inicializamos el RecyclerView y el adapter con la data obtenida
            initAdapter();
        }
    }

    private class UltimoAlbaranAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            //obtenemos el listado de Clientes desde el servidor
            ultimoAlbaran = operacionesServidor.ultimaCabeceraAlbaran(context, idTrabajador);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        /*
        Crear un nuevo adaptador
         */

            mCabeceraAlbaranes = operacionesServidor.verListaAlbaranesCabeceraCompleta(context);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();

        new UltimoAlbaranAsync().execute();




    }

    public void crearNuevaTemporadaAlbaran(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText etIdAlbaran = new EditText(ListaAlbaranesCabecera.this);


        etIdAlbaran.setHint("codigo");
        builder.setMessage(getString(R.string.mensaje_nueva_temporada_pasword))
                .setTitle(this.getString(R.string.nueva_temporada))
                .setCancelable(false)
                .setView(etIdAlbaran)
                .setNegativeButton(this.getString(R.string.Cancelar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton(this.getString(R.string.continuar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                int nuevoNumAlbaran = Integer.parseInt(etIdAlbaran.getText().toString());
                                nuevoNumAlbaran--;
                                mOperacionesBaseDatos.nuevaCabeceraAlbaran(nuevoNumAlbaran,idTrabajador);

                                int nuevoAlbaran = nuevoNumAlbaran + 1;
                                String nuevoCodigoAlbaran = idTrabajador + String.valueOf(nuevoAlbaran);
                                Intent intento = new Intent(ListaAlbaranesCabecera.this, FormularioAlbaranesCabecera.class);
                                intento.putExtra("codigoAlbaran", nuevoCodigoAlbaran);
                                startActivity(intento);








                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }
}
