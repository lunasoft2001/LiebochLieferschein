package at.ums.luna.liebochlieferschein.servidor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.ums.luna.liebochlieferschein.database.DBHelper;
import at.ums.luna.liebochlieferschein.inicio.MainActivity;
import at.ums.luna.liebochlieferschein.modelos.CabeceraAlbaranes;
import at.ums.luna.liebochlieferschein.modelos.Clientes;

/**
 * Created by luna-aleixos on 09.11.2016.
 */

public class OperacionesServidor {

    public OperacionesServidor(){}

    /**
     * CLIENTES
     */

    public List<Clientes> verListaClientesServidor(final Context context){

        final ArrayList<Clientes> listaClientes = new ArrayList<>();

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
                                listaClientes.add(cliente);

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

        return listaClientes;
    }

    public ArrayList<Clientes> verListaClientesServidor1(final Context context){

        final ArrayList<Clientes> listaClientes = new ArrayList<>();

        final int[] acabado = {0};

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Defaults.SERVER_URL + "obtener_clientes_1.php", (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            int count = 0;
                            int total = response.getInt("total");
                            Log.i("JJ","Hay " + total + " registros");

                            JSONArray jsonArray = response.getJSONArray("clientes");
                            while (count < total) {
                                JSONObject clientesObtenidos = jsonArray.getJSONObject(count);

                                Clientes cliente = new Clientes(clientesObtenidos.getInt("idCliente"),
                                        clientesObtenidos.getString("nombre"),
                                        clientesObtenidos.getString("direccion"),
                                        clientesObtenidos.getString("telefono"),
                                        clientesObtenidos.getString("email"));
                                listaClientes.add(cliente);
                                Log.i("JJ", String.valueOf(clientesObtenidos.getInt("idCliente")));

                                count++;
                            }

                            if (listaClientes.size()==total){
                                acabado[0] =1;}
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("JJ",e.toString());
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(context).addToRequestque(jsonObjectRequest);

        while (acabado[0]==0) {
            Log.i("JJ", "total " + String.valueOf(listaClientes.size()));
        }
        return listaClientes;
    }


    /**
     * ALBARANES CABECERA
     */

    public int obtenerUltimoAlbaran(String idTrabajadorActual, final Context context){
        final int[] ultimoAlbaran = new int[1];
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Defaults.SERVER_URL + "obtener_ultimo_id_cabecera.php?idTrabajador=" + idTrabajadorActual,
                (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    ultimoAlbaran[0] = response.getInt("Max(id)");

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

        int respuesta = ultimoAlbaran[0];
        return respuesta;

    }

    public List<CabeceraAlbaranes> verListaAlbaranesCabeceraCompleta(final Context context){

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

    public void nuevaCabeceraAlbaran(final Context context, int ultimoAlbaran, final String idTrabajador){

        int nuevoAlbaran = ultimoAlbaran + 1;
        final String nuevoCodigoAlbaran = idTrabajador + String.valueOf(nuevoAlbaran);

        final int numeroApasar = nuevoAlbaran;

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Defaults.SERVER_URL + "insertar_cabecera_albaran.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Lieferschein gemacht", Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "error", Toast.LENGTH_LONG).show();
                error.printStackTrace();

            }
        })
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {

                JSONObject jsonBody = new JSONObject();

                try {
                    jsonBody.put("id",String.valueOf(numeroApasar));
                    jsonBody.put("idTrabajador",idTrabajador);
                    jsonBody.put("codigoAlbaran",nuevoCodigoAlbaran);
                    jsonBody.put("idCliente","1");
                    jsonBody.put("fecha",obtenerFechaActual());
                    jsonBody.put("recogida","abholung");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final String mRequestBody = jsonBody.toString();

                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };
        MySingleton.getInstance(context).addToRequestque(stringRequest);

    }

    private String obtenerFechaActual() {

        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd.MM.yyyy");
        return formateador.format(ahora);
    }

}
