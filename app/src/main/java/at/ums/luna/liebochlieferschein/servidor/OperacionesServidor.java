package at.ums.luna.liebochlieferschein.servidor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.ums.luna.liebochlieferschein.R;
import at.ums.luna.liebochlieferschein.actividades.capturarFirma;
import at.ums.luna.liebochlieferschein.database.DBHelper;
import at.ums.luna.liebochlieferschein.inicio.MainActivity;
import at.ums.luna.liebochlieferschein.modelos.CabeceraAlbaranes;
import at.ums.luna.liebochlieferschein.modelos.Clientes;
import at.ums.luna.liebochlieferschein.modelos.DetalleAlbaranes;

/**
 * Created by luna-aleixos on 09.11.2016.
 */

public class OperacionesServidor {

    public OperacionesServidor(){}

    /**
     * CLIENTES
     */

    // este metodo queda como modelo para llamadas con php sin objetos dentro de JSON
//    public List<Clientes> verListaClientesServidor(final Context context){
//
//        final ArrayList<Clientes> listaClientes = new ArrayList<>();
//
//        JsonArrayRequest jsonArrayRequest =  new JsonArrayRequest(Request.Method.POST, Defaults.SERVER_URL + "obtener_clientes.php", (String) null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        int count = 0;
//                        while (count < response.length()){
//                            try {
//                                JSONObject jsonObject = response.getJSONObject(count);
//                                Clientes cliente = new Clientes(jsonObject.getInt("idCliente"),
//                                        jsonObject.getString("nombre"),
//                                        jsonObject.getString("direccion"),
//                                        jsonObject.getString("telefono"),
//                                        jsonObject.getString("email"));
//                                listaClientes.add(cliente);
//
//                                count++;
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(context,"Error....", Toast.LENGTH_SHORT).show();
//                error.printStackTrace();
//
//            }
//        });
//
//        MySingleton.getInstance(context).addToRequestque(jsonArrayRequest);
//
//        return listaClientes;
//    }

    public ArrayList<Clientes> verListaClientesServidor1(final Context context){

        final ArrayList<Clientes> listaClientes = new ArrayList<>();

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

        return listaClientes;
    }


    /**
     * ALBARANES CABECERA
     */

//    public int obtenerUltimoAlbaran(String idTrabajadorActual, final Context context){
//        final int[] ultimoAlbaran = new int[1];
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
//                Defaults.SERVER_URL + "obtener_ultimo_id_cabecera.php?idTrabajador=" + idTrabajadorActual,
//                (String) null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                try {
//                    ultimoAlbaran[0] = response.getInt("Max(id)");
//
//                }catch (JSONException e){
//                    e.printStackTrace();
//
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, "Algo salio mal " + error,Toast.LENGTH_SHORT).show();
//                error.printStackTrace();
//
//            }
//        });
//
//        MySingleton.getInstance(context).addToRequestque(jsonObjectRequest);
//
//        int respuesta = ultimoAlbaran[0];
//        return respuesta;
//
//    }

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

    public void eliminarCabeceraAlbaran(final Context context,  final String codigoAlbaran){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Defaults.SERVER_URL + "borrar_cabecera_albaran.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Lieferschein abgelöscht", Toast.LENGTH_LONG).show();

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
                    jsonBody.put("codigoAlbaran",codigoAlbaran);
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

    public void editarCabeceraAlbaran(final Context context, final String codigoAlbaran, final String fecha,
                                      final String idCliente, final String recogida){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Defaults.SERVER_URL + "actualizar_cabecera_albaran.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Lieferschein updated", Toast.LENGTH_LONG).show();

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
                    jsonBody.put("codigoAlbaran",codigoAlbaran);
                    jsonBody.put("fecha",fecha);
                    jsonBody.put("idCliente",idCliente);
                    jsonBody.put("recogida",recogida);
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

    public CabeceraAlbaranes obtenerAlbaranCompleto(final Context context, String codigoAlbaranObtenido){

        final CabeceraAlbaranes cabeceraAlbaranes = new CabeceraAlbaranes();

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Defaults.SERVER_URL + "obtener_cabecera_albaran_por_id.php?codigoAlbaran=" + codigoAlbaranObtenido,
                (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    cabeceraAlbaranes.setId(Integer.parseInt(response.getString("id")));
                    cabeceraAlbaranes.setCodigoAlbaran(response.getString("codigoAlbaran"));
                    cabeceraAlbaranes.setFecha(response.getString("fecha"));
                    cabeceraAlbaranes.setIdCliente(Integer.parseInt(response.getString("idCliente")));
                    cabeceraAlbaranes.setNombreCliente(response.getString("nombre"));
                    cabeceraAlbaranes.setIdTrabajador(response.getString("idTrabajador"));
                    cabeceraAlbaranes.setRecogida(response.getString("recogida"));
                    cabeceraAlbaranes.setDireccionCliente(response.getString("direccion"));
                    cabeceraAlbaranes.setEmailCliente(response.getString("email"));
                    cabeceraAlbaranes.setTelefonoCliente(response.getString("telefono"));

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("JJ", error.toString());
                Toast.makeText(context, "Algo salio mal " + error,Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        });

        MySingleton.getInstance(context).addToRequestque(jsonObjectRequest);

        return cabeceraAlbaranes;
    }


    /**
     * DETALLE ALBARANES
     */

    public List<DetalleAlbaranes> verListaDetalleAlbaran(final Context context, String codigoAlbaran){

        final ArrayList<DetalleAlbaranes> arrayList = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest =  new JsonArrayRequest(Request.Method.POST,
                Defaults.SERVER_URL + "obtener_detalles_albaran.php?codigoAlbaran=" + codigoAlbaran,
                (String) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int count = 0;
                        while (count < response.length()){
                            try {
                                JSONObject jsonObject = response.getJSONObject(count);
                                DetalleAlbaranes detalle = new DetalleAlbaranes(jsonObject.getString("codigoAlbaran"),
                                        jsonObject.getInt("linea"),
                                        jsonObject.getString("detalle"),
                                        jsonObject.getDouble("cantidad"),
                                        jsonObject.getString("tipo"));
                                arrayList.add(detalle);

                                count++;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(context,"Error.... en detalle", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                DetalleAlbaranes detalle = new DetalleAlbaranes("error",0,"error",0,"error");
                arrayList.add(detalle);

            }
        });

        MySingleton.getInstance(context).addToRequestque(jsonArrayRequest);

        return arrayList;
    }

    public void nuevoDetalleAlbaran(final Context context, int ultimaLinea, final String codigoAlbaran){

        final int nuevodetalle = ultimaLinea + 1;

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Defaults.SERVER_URL + "insertar_detalle_albaran.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Detail gemacht", Toast.LENGTH_LONG).show();

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
                    jsonBody.put("codigoAlbaran",codigoAlbaran);
                    jsonBody.put("linea",String.valueOf(nuevodetalle));
                    jsonBody.put("detalle","");
                    jsonBody.put("cantidad","0");
                    jsonBody.put("tipo","");
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


/**
 * EXTRAS
 */

    private String obtenerFechaActual() {

        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd.MM.yyyy");
        return formateador.format(ahora);
    }

    //android upload file to server
    public int uploadFile(final String selectedFilePath, Activity activity, final Context context){

        int serverResponseCode = 0;
        String SERVER_URL = "http://77.119.243.100/upload-test/UploadToServer.php";

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length-1];

        if (!selectedFile.isFile()){
//            dialog.dismiss();

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("JJ","Source File Doesn't Exist: " + selectedFilePath);
                }
            });
            return 0;
        }else{
            try{
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file",selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.i("JJ", "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if(serverResponseCode == 200){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(context,"File Upload completed.",Toast.LENGTH_LONG).show();
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();



            } catch (FileNotFoundException e) {
                e.printStackTrace();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,"File Not Found",Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(context, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
//            dialog.dismiss();
            return serverResponseCode;
        }

    }

}
