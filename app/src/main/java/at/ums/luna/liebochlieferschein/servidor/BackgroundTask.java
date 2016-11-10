package at.ums.luna.liebochlieferschein.servidor;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import at.ums.luna.liebochlieferschein.actividades.Preferencias;
import at.ums.luna.liebochlieferschein.database.DBHelper;
import at.ums.luna.liebochlieferschein.inicio.MainActivity;
import at.ums.luna.liebochlieferschein.modelos.CabeceraAlbaranes;
import at.ums.luna.liebochlieferschein.modelos.Clientes;

/**
 * Created by luna-aleixos on 07.11.2016.
 */

public class BackgroundTask {

    Context context;

    int ultimoAlbaran;

    public BackgroundTask(Context context){
        this.context = context;
    }

    public ArrayList<Clientes> getListaClientes(){

        final ArrayList<Clientes> arrayList = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest =  new JsonArrayRequest(Request.Method.POST, Defaults.SERVER_URL + "obtener_clientes.php", (String) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int count = 0;
                        while (count < response.length()){
                            try {
                                JSONObject jsonObject = response.getJSONObject(count);
                                Clientes cliente = new Clientes(jsonObject.getInt("idCliente"),
                                        jsonObject.getString("nombre"),
                                        jsonObject.getString("direccion"),
                                        jsonObject.getString("telefono"),
                                        jsonObject.getString("email"));
                                arrayList.add(cliente);

                                count++;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context,"Error....", Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        });

        MySingleton.getInstance(context).addToRequestque(jsonArrayRequest);

        return arrayList;
    }

    public ArrayList<CabeceraAlbaranes> getListaAlbaranesCabecera(){

        final ArrayList<CabeceraAlbaranes> arrayList = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest =  new JsonArrayRequest(Request.Method.POST, Defaults.SERVER_URL + "obtener_cabecera_albaranes.php", (String) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int count = 0;
                        while (count < response.length()){
                            try {
                                JSONObject jsonObject = response.getJSONObject(count);
                                CabeceraAlbaranes cabecera = new CabeceraAlbaranes(jsonObject.getInt("id"),
                                        jsonObject.getString("fecha"),
                                        jsonObject.getInt("idCliente"),
                                        jsonObject.getString("idTrabajador"),
                                        jsonObject.getString("codigoAlbaran"),
                                        jsonObject.getString("recogida"),
                                        jsonObject.getString("nombre"),
                                        jsonObject.getString("direccion"),
                                        jsonObject.getString("email"),
                                        jsonObject.getString("telefono"));
                                arrayList.add(cabecera);

                                count++;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context,"Error....", Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        });

        MySingleton.getInstance(context).addToRequestque(jsonArrayRequest);

        return arrayList;
    }

    public int getUltimoAlbaran(String idTrabajador){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Defaults.SERVER_URL + "obtener_ultimo_id_cabecera.php?idTrabajador=" + idTrabajador,
                (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ultimoAlbaran = response.getInt("Max(id)");
                    
                    Log.i("JJ", "ultimo 1 " +  String.valueOf(ultimoAlbaran));

                }catch (JSONException e){
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Algo salio mal " + error,Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        });
        MySingleton.getInstance(context).addToRequestque(jsonObjectRequest);
        Log.i("JJ", "ultimo 2 " +  String.valueOf(ultimoAlbaran));


        return ultimoAlbaran;
    }


    /**
     * ESTE METODO ACTUALIZA LA BASE DE DATOS SQLite. Lo desprecio para usar siempre conexiones al servidor.
     */

    public void actualizaListaClientesEnTelefono(){

        DBHelper dbHelper = new DBHelper(context);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        JsonArrayRequest jsonArrayRequest =  new JsonArrayRequest(Request.Method.POST, Defaults.SERVER_URL + "obtener_clientes.php", (String) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int count = 0;
                        Log.i("JJ", "Se han encomtrado " + response.length() + " registros.");
                        while (count < response.length()){
                            try {
                                JSONObject jsonObject = response.getJSONObject(count);
                                ContentValues valor = new ContentValues();
                                valor.put(DBHelper.Clientes.ID,jsonObject.getInt("idCliente"));
                                valor.put(DBHelper.Clientes.NOMBRE,jsonObject.getString("nombre"));
                                valor.put(DBHelper.Clientes.DIRECCION,jsonObject.getString("direccion"));
                                valor.put(DBHelper.Clientes.TELEFONO,jsonObject.getString("telefono"));
                                valor.put(DBHelper.Clientes.EMAIL,jsonObject.getString("email"));

                                db.insert(DBHelper.Tablas.CLIENTES,null,valor);

                                count++;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context,"Error....", Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        });

        MySingleton.getInstance(context).addToRequestque(jsonArrayRequest);

    }

}