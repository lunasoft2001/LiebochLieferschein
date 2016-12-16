package at.ums.luna.liebochlieferschein.actividades;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import at.ums.luna.liebochlieferschein.R;
import at.ums.luna.liebochlieferschein.database.OperacionesBaseDatos;
import at.ums.luna.liebochlieferschein.modelos.DetalleAlbaranes;
import at.ums.luna.liebochlieferschein.servidor.Defaults;
import at.ums.luna.liebochlieferschein.servidor.MySingleton;
import at.ums.luna.liebochlieferschein.servidor.OperacionesServidor;

public class FormularioAlbaranesDetalle extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView codigoAlbaran;
    private TextView linea;
    private TextView detalle;
    private TextView cantidad;
    private TextView tipo;
    private Spinner spinnerEH;
    private int changeSpinner = -1;

    private String valorCodigoAlbaran;
    private String valorLinea;

    private OperacionesBaseDatos mOperacionesBaseDatos;
    private OperacionesServidor mOperacionesServidor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_albaranes_detalle);

        codigoAlbaran = (TextView) findViewById(R.id.tvCodigoAlbaran);
        linea = (TextView) findViewById(R.id.linea);
        detalle = (TextView) findViewById(R.id.detalle);
        cantidad = (TextView) findViewById(R.id.cantidad);
        tipo = (TextView) findViewById(R.id.tipo);
        spinnerEH = (Spinner) findViewById(R.id.spinnerEH);

        //llenamos el spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.eh_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEH.setAdapter(adapter);
        spinnerEH.setOnItemSelectedListener(this);

        //Obtenemos los valores a mostrar

        Intent intento = getIntent();

        Bundle bundle = intento.getExtras();
        if(bundle != null) {
            valorCodigoAlbaran = bundle.getString("codigoAlbaran");
            valorLinea = bundle.getString("linea");
        }

        mOperacionesBaseDatos = new OperacionesBaseDatos(this);
        mOperacionesServidor = new OperacionesServidor();

        obtenerDatos();

        //cierra el teclado al hacer click

        cantidad.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }

                return false;
            }
        });


        tipo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }

                return false;
            }
        });


        //Codigo para el onClickListener
        findViewById(R.id.botonCancelarDetalle).setOnClickListener(mGlobal_onClickListener);
        findViewById(R.id.botonBorrarDetalle).setOnClickListener(mGlobal_onClickListener);
        findViewById(R.id.botonActualizarDetalle).setOnClickListener(mGlobal_onClickListener);

    }

    //Intents para cualquier botón de la actividad
    final View.OnClickListener mGlobal_onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.botonCancelarDetalle:
                    finish();
                    break;
                case R.id.botonBorrarDetalle:
                    Eliminar();
                    break;
                case R.id.botonActualizarDetalle:
                    Actualizar();
                    break;
            }
        }
    };


    private void obtenerDatos(){

        //Obtiene los datos del servidor

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Defaults.SERVER_URL + "obtener_detalle_albaran_por_id.php?codigoAlbaran=" + valorCodigoAlbaran + "&linea=" + valorLinea,
                (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    codigoAlbaran.setText(response.getString("codigoAlbaran"));
                    linea.setText(String.valueOf(response.getString("linea")));
                    detalle.setText(response.getString("detalle"));
                    cantidad.setText(String.valueOf(response.getDouble("cantidad")));
                    tipo.setText(response.getString("tipo"));
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("JJ", error.toString());
                Toast.makeText(FormularioAlbaranesDetalle.this, "Algo salio mal " + error,Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        });

        MySingleton.getInstance(this).addToRequestque(jsonObjectRequest);
    }

    private void Eliminar(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(this.getString(R.string.ContinuarBorrando))
                .setTitle(this.getString(R.string.Advertencia))
                .setCancelable(false)
                .setNegativeButton(this.getString(R.string.Cancelar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton(this.getString(R.string.continuar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

//                                mOperacionesBaseDatos.eliminarDetalleAlbaran(valorCodigoAlbaran,valorLinea, FormularioAlbaranesDetalle.this);
                                mOperacionesServidor.eliminarDetalleAlbaran(FormularioAlbaranesDetalle.this, valorCodigoAlbaran, valorLinea);

                                finish();

                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void Actualizar(){

        String detalleActual = detalle.getText().toString();
        String cantidadActual = cantidad.getText().toString();
        String tipoActual = tipo.getText().toString();

//        mOperacionesBaseDatos.actualizarDetalleAlbaran(valorCodigoAlbaran,valorLinea,detalleActual,
//                cantidadActual,tipoActual);

        new cerrarAsync().execute();

    }

    private class cerrarAsync extends AsyncTask<Void, Void, Void> {

        String detalleActual = detalle.getText().toString();
        String cantidadActual = cantidad.getText().toString();
        String tipoActual = tipo.getText().toString();

        @Override
        protected Void doInBackground(Void... params) {

            //actualizamos y esperamos respuesta

            ArrayList<String> acabado = new ArrayList<>();
            acabado =  mOperacionesServidor.editarDetalleAlbaran(FormularioAlbaranesDetalle.this, valorCodigoAlbaran, valorLinea, detalleActual,
                    cantidadActual, tipoActual);

            while ( acabado.size()==0){}

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //cierra el formulario
            finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(changeSpinner != -1) {
            String valorObtenido = (String) parent.getItemAtPosition(position);
            tipo.setText(valorObtenido);
        }
        changeSpinner = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }
}
