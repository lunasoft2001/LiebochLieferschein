package at.ums.luna.liebochlieferschein.servidor;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import at.ums.luna.liebochlieferschein.database.DBHelper;
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

    public void nuevaCabeceraAlbaran(final Context context, final String id,  final String idTrabajador, final String codigoAlbaran,
                                     final String idCliente, final String fecha, final String recogida){

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Defaults.SERVER_URL + "insertar_cabecera_albaran.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, response, Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "error", Toast.LENGTH_LONG).show();
                error.printStackTrace();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String, String>();
//                params.put("id",id);
//                params.put("idTrabajador",idTrabajador);
//                params.put("codigoAlbaran",codigoAlbaran);
//                params.put("idCliente",idCliente);
//                params.put("fecha",fecha);
//                params.put("recogida",recogida);

                params.put("id","170001");
                params.put("idTrabajador","JJ");
                params.put("codigoAlbaran","JJ170001");
                params.put("idCliente","1");
                params.put("fecha","14.11.16");
                params.put("recogida","abholung");

                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestque(stringRequest);

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
