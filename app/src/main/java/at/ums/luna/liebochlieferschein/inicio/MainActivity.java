package at.ums.luna.liebochlieferschein.inicio;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import at.ums.luna.liebochlieferschein.R;
import at.ums.luna.liebochlieferschein.actividades.ListaAlbaranesCabecera;
import at.ums.luna.liebochlieferschein.actividades.ListaClientes;
import at.ums.luna.liebochlieferschein.actividades.Preferencias;
import at.ums.luna.liebochlieferschein.database.OperacionesBaseDatos;
import at.ums.luna.liebochlieferschein.servidor.Defaults;
import at.ums.luna.liebochlieferschein.servidor.MySingleton;
import at.ums.luna.liebochlieferschein.servidor.OperacionesServidor;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;


    private OperacionesServidor operacionesServidor;


    private TextView tvUltimoAlbaran;
    private Toolbar toolbar;

    private String idTrabajadorActual;
    private String nombreTrabajadorActual;
    private String userActual;
    private String passActual;

    private int ultimoAlbaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Cargar settings por defecto
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

        //activamos la toolbar
        setToolbar();

        //permisos
        solicitarPermisos();
    }

    @Override
    protected void onResume() {
        super.onResume();

        tvUltimoAlbaran = (TextView) findViewById(R.id.tvMaxAlbaran);

        operacionesServidor = new OperacionesServidor();

        //rellenamos trabajadorActual
        mostrarPreferencias();

        toolbar.setTitle(String.format(this.getString(R.string.Trabajador), nombreTrabajadorActual));

        //comprobacion si no esta configurado el trabajador
        if (idTrabajadorActual.equals("ßß")){
            Intent intento = new Intent(this, Preferencias.class);
            startActivity(intento);
        }

        //rellenamos ultimoAlbaran
        obtenerUltimoAlbaran();


    }

    private void obtenerUltimoAlbaran(){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Defaults.SERVER_URL + "obtener_ultimo_id_cabecera.php?idTrabajador=" + idTrabajadorActual,
                (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    ultimoAlbaran = response.getInt("Max(id)");
                    tvUltimoAlbaran.setText(String.valueOf(ultimoAlbaran));

                }catch (JSONException e){
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Algo salio mal " + error,Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        });

        MySingleton.getInstance(MainActivity.this).addToRequestque(jsonObjectRequest);
    }

    private void solicitarPermisos() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        }
    }

    private void setToolbar(){
        //añadir la Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
    }

    public void botonListaClientes(View v) {
        Intent intento = new Intent(this, ListaClientes.class);
        startActivity(intento);
    }

//    public void botonActualizarBdClientes(View v) {
//
//        mOperacionesBaseDatos.actualizarClientes(this);
//    }

    public void botonEditarTrabajador(View v){
        Intent intento = new Intent(this, Preferencias.class);
        startActivity(intento);
    }

    public void botonListaAlbaranes(View v){
        Intent intento = new Intent(this, ListaAlbaranesCabecera.class);
        intento.putExtra("idTrabajador", idTrabajadorActual);
        startActivity(intento);
    }

    public void botonNuevaTemporadaAlbaranComprobacion(View v){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText etPassword = new EditText(MainActivity.this);

        etPassword.setHint("password");

        builder.setMessage(getString(R.string.mensaje_nueva_temporada_pasword))
                .setTitle(this.getString(R.string.nueva_temporada))
                .setCancelable(false)
                .setView(etPassword)
                .setNegativeButton(this.getString(R.string.Cancelar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton(this.getString(R.string.continuar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String introPass = etPassword.getText().toString();

                                if (introPass.equals("1234")){
                                    crearNuevaTemporadaAlbaran();


                                } else {
                                    Toast.makeText(MainActivity.this, R.string.pass_false,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void crearNuevaTemporadaAlbaran(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText etIdAlbaran = new EditText(MainActivity.this);


        etIdAlbaran.setHint("codigo");
        builder.setMessage(getString(R.string.mensaje_nueva_temporada_codigo))
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
                                operacionesServidor.nuevaCabeceraAlbaran(MainActivity.this,nuevoNumAlbaran,idTrabajadorActual);

                                botonListaAlbaranes(null);


                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }

    public void mostrarPreferencias(){
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(this);

        idTrabajadorActual = pref.getString("prefijo","?");
        nombreTrabajadorActual = pref.getString("nombre","?");
        userActual = pref.getString("usuario","?");
        passActual = pref.getString("password","?");

    }
}