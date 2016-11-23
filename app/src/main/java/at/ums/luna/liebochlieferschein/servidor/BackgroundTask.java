package at.ums.luna.liebochlieferschein.servidor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    public ArrayList<CabeceraAlbaranes> obtenerCabeceraAlbaran(String codigoAlbaran){


        final ArrayList<CabeceraAlbaranes> arrayList = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest =  new JsonArrayRequest(Request.Method.POST,
                Defaults.SERVER_URL + "obtener_cabecera_albaran_por_id.php?codigoAlbaran=" + codigoAlbaran,
                (String) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int count = 0;
                        while (count < response.length()){
                            try {
                                JSONObject jsonObject = response.getJSONObject(count);
                                CabeceraAlbaranes cabecera = new CabeceraAlbaranes(jsonObject.getInt("id"),
                                        jsonObject.getString("codigoAlbaran"),
                                        jsonObject.getString("fecha"),
                                        jsonObject.getInt("idCliente"),
                                        jsonObject.getString("nombre"),
                                        jsonObject.getString("idTrabajador"),
                                        jsonObject.getString("recogida"),
                                        jsonObject.getString("direccion"),
                                        jsonObject.getString("telefono"),
                                        jsonObject.getString("email"));
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
