package at.ums.luna.liebochlieferschein.database;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.ums.luna.liebochlieferschein.R;
import at.ums.luna.liebochlieferschein.modelos.CabeceraAlbaranes;
import at.ums.luna.liebochlieferschein.modelos.Clientes;
import at.ums.luna.liebochlieferschein.modelos.DetalleAlbaranes;
import at.ums.luna.liebochlieferschein.servidor.BackgroundTask;
import at.ums.luna.liebochlieferschein.servidor.OperacionesServidor;


/**
 * Created by luna-aleixos on 30.05.2016.
 */
public class OperacionesBaseDatos {
    DBHelper dbHelper;
    SQLiteDatabase db;

    public OperacionesBaseDatos(Context context){
        dbHelper = new DBHelper(context);
    }

    public void abrir(){
        Log.i("JUANJO", "BD abierta para escribir");
        db = dbHelper.getWritableDatabase();
    }

    public void leer(){
        Log.i("JUANJO", "BD abierta para leer");
        db = dbHelper.getReadableDatabase();
    }

    public void cerrar(){
        Log.i("JUANJO", "BD cerrada");
        dbHelper.close();
    }


    /**
     * CLIENTES
     */
    public static final String[] todasColumnasClientes = {
            DBHelper.Clientes.ID,
            DBHelper.Clientes.NOMBRE,
            DBHelper.Clientes.DIRECCION,
            DBHelper.Clientes.TELEFONO,
            DBHelper.Clientes.EMAIL
    };

    public List<Clientes> verListaClientesCompleta(){
        leer();
        Cursor cursor = db.query(DBHelper.Tablas.CLIENTES,todasColumnasClientes,null,null,null,null,null);

        List<Clientes> listaClientes = new ArrayList<>();
        while (cursor.moveToNext()){
            Clientes clientes = new Clientes();
            clientes.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.Clientes.ID)));
            clientes.setNombre(cursor.getString(cursor.getColumnIndex(DBHelper.Clientes.NOMBRE)));
            clientes.setDireccion(cursor.getString(cursor.getColumnIndex(DBHelper.Clientes.DIRECCION)));
            clientes.setTelefono(cursor.getString(cursor.getColumnIndex(DBHelper.Clientes.TELEFONO)));
            clientes.setEmail(cursor.getString(cursor.getColumnIndex(DBHelper.Clientes.EMAIL)));
            listaClientes.add(clientes);
        }
        cerrar();
        cursor.close();

        return listaClientes;
    }

    public List<Clientes> verListaClientesCompletaPorNombre(){
        leer();
        Cursor cursor = db.query(DBHelper.Tablas.CLIENTES,todasColumnasClientes,null,null,null,null,DBHelper.Clientes.NOMBRE);

        List<Clientes> listaClientes = new ArrayList<>();
        while (cursor.moveToNext()){
            Clientes clientes = new Clientes();
            clientes.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.Clientes.ID)));
            clientes.setNombre(cursor.getString(cursor.getColumnIndex(DBHelper.Clientes.NOMBRE)));
            clientes.setDireccion(cursor.getString(cursor.getColumnIndex(DBHelper.Clientes.DIRECCION)));
            clientes.setTelefono(cursor.getString(cursor.getColumnIndex(DBHelper.Clientes.TELEFONO)));
            clientes.setEmail(cursor.getString(cursor.getColumnIndex(DBHelper.Clientes.EMAIL)));
            listaClientes.add(clientes);
        }
        cerrar();
        cursor.close();

        return listaClientes;
    }


    public ArrayList<String> datosCampoClientes(String[] nombreColumna){
        leer();
        Cursor cursor = db.query(DBHelper.Tablas.CLIENTES,nombreColumna,null,null,null,null,null);
        cursor.moveToFirst();
        ArrayList<String> lista = new ArrayList<String>();
        do{
            lista.add(cursor.getString(0));
        }while (cursor.moveToNext());

        return lista;
    }


    public Clientes obtenerClienteAlElegirEnDialogo(String textoObtenido) {
        leer();

        String[] columnasAMostrar = {DBHelper.Clientes.ID};
        String[] args ={textoObtenido};
        String criterioSeleccion = DBHelper.Clientes.NOMBRE + "=?";

        Cursor cursor = db.query(DBHelper.Tablas.CLIENTES,columnasAMostrar,criterioSeleccion,args,null,null,null);

        cursor.moveToFirst();

        int resultado = cursor.getInt(cursor.getColumnIndex(DBHelper.Clientes.ID));
        

        cursor.close();

        String[] args1 ={String.valueOf(resultado)};
        String criterioSeleccion1 = DBHelper.Clientes.ID + "=?";

        Cursor cursor1 = db.query(DBHelper.Tablas.CLIENTES,todasColumnasClientes,criterioSeleccion1,args1,null,null,null);

        cursor1.moveToFirst();

        Clientes cliente = new Clientes();
        cliente.setId(cursor1.getInt(cursor1.getColumnIndex(DBHelper.Clientes.ID)));
        cliente.setNombre(cursor1.getString(cursor1.getColumnIndex(DBHelper.Clientes.NOMBRE)));
        cliente.setDireccion(cursor1.getString(cursor1.getColumnIndex(DBHelper.Clientes.DIRECCION)));

        cursor1.close();
        cerrar();
        return cliente;
    }


    public List<Clientes> verListaClientesServidor(Context context){
        OperacionesServidor operacionesServidor = new OperacionesServidor();
        List<Clientes> listaClientes = operacionesServidor.verListaClientesServidor1(context);

        return listaClientes;
    }



    public void actualizarClientes(final Context context){
        abrir();

//        final ProgressDialog circuloProgreso=ProgressDialog.show(context,"",context.getString(R.string.espere), true);
//        circuloProgreso.setCancelable(false);


        //borra los datos
        db.delete(DBHelper.Tablas.CLIENTES, null, null);


        //actualiza los datos
        BackgroundTask backgroundTask = new BackgroundTask(context);
        backgroundTask.actualizaListaClientesEnTelefono();

    }




    /**
     * ALBARANES CABECERA
     */

    public List<CabeceraAlbaranes> verListaAlbaranesCabeceraCompleta(){
        leer();

        String querySimple = "SELECT cabecera_albaranes.codigoAlbaran, " +
                "cabecera_albaranes.fecha, clientes.nombre " +
                "FROM clientes INNER JOIN cabecera_albaranes " +
                "ON clientes.id = cabecera_albaranes.idCliente " +
                "ORDER BY codigoAlbaran DESC";

        Cursor cursor = db.rawQuery(querySimple,null,null);

        List<CabeceraAlbaranes> listaAlbaranes = new ArrayList<>();
        while (cursor.moveToNext()){

            CabeceraAlbaranes cabeceraAlbaranes = new CabeceraAlbaranes();
            cabeceraAlbaranes.setCodigoAlbaran(cursor.getString(cursor.getColumnIndex(DBHelper.CabeceraAlbaranesColumnas.CODIGO_ALBARAN)));
            cabeceraAlbaranes.setFecha(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.CabeceraAlbaranesColumnas.FECHA)));
            cabeceraAlbaranes.setNombreCliente(cursor.getString(cursor.getColumnIndex("nombre")));
            listaAlbaranes.add(cabeceraAlbaranes);
        }

        cerrar();
        cursor.close();
        return listaAlbaranes;
    }


    public CabeceraAlbaranes obtenerCabeceraAlbaran(String codigoAlbaran){
        leer();

        String sql = "SELECT cabecera_albaranes.codigoAlbaran, " +
                "cabecera_albaranes.fecha, clientes.nombre, " +
                "cabecera_albaranes.idCliente, " +
                "clientes.direccion, " +
                "clientes.email, " +
                "cabecera_albaranes.recogida " +
                "FROM clientes INNER JOIN cabecera_albaranes " +
                "ON clientes.id = cabecera_albaranes.idCliente " +
                "WHERE cabecera_albaranes.codigoAlbaran =?";
        String[] selectionArgs = {codigoAlbaran};

        Cursor c = db.rawQuery(sql,selectionArgs);

        CabeceraAlbaranes albaran = new CabeceraAlbaranes();

        c.moveToFirst();

        albaran.setCodigoAlbaran(c.getString(c.getColumnIndex("codigoAlbaran")));
        albaran.setFecha(c.getString(c.getColumnIndex("fecha")));
        albaran.setNombreCliente(c.getString(c.getColumnIndex("nombre")));
        albaran.setIdCliente(c.getInt(c.getColumnIndex("idCliente")));
        albaran.setDireccionCliente(c.getString(c.getColumnIndex("direccion")));
        albaran.setEmailCliente(c.getString(c.getColumnIndex("email")));
        albaran.setRecogida(c.getString(c.getColumnIndex("recogida")));

        cerrar();
        c.close();

        return albaran;

    }

    public int ultimaCabeceraAlbaran(){
        leer();
        String sql = "SELECT Max(id) As idMaximo From cabecera_albaranes";

        Cursor c = db.rawQuery(sql,null);

        c.moveToFirst();

        int ultimoAlbaran = c.getInt(c.getColumnIndex("idMaximo"));

        cerrar();
        c.close();
        return ultimoAlbaran;
    }



    public void nuevaCabeceraAlbaran(int ultimoAlbaran, String idTrabajador){
        abrir();

        int nuevoAlbaran = ultimoAlbaran + 1;
        String nuevoCodigoAlbaran = idTrabajador + String.valueOf(nuevoAlbaran);

        ContentValues valores = new ContentValues();
        valores.put(DBHelper.CabeceraAlbaranesColumnas.ID, nuevoAlbaran);
        valores.put(DBHelper.CabeceraAlbaranesColumnas.ID_TRABAJADOR, idTrabajador);
        valores.put(DBHelper.CabeceraAlbaranesColumnas.CODIGO_ALBARAN,nuevoCodigoAlbaran);
        valores.put(DBHelper.CabeceraAlbaranesColumnas.FECHA,obtenerFechaActual());
        valores.put(DBHelper.CabeceraAlbaranesColumnas.ID_CLIENTE,1);
        valores.put(DBHelper.CabeceraAlbaranesColumnas.RECOGIDA,"Abholung");

        db.insert(DBHelper.Tablas.CABECERA_ALBARANES,null,valores);



    }

    private String obtenerFechaActual() {

        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd.MM.yyyy");
        return formateador.format(ahora);

    }

    public void actualizarCabeceraAlbaran(String[] idAlbaran, String fecha, int idCliente, String recogida){
        abrir();
        ContentValues actualizar = new ContentValues();
        actualizar.put(DBHelper.CabeceraAlbaranesColumnas.FECHA, fecha);
        actualizar.put(DBHelper.CabeceraAlbaranesColumnas.ID_CLIENTE, idCliente);
        actualizar.put(DBHelper.CabeceraAlbaranesColumnas.RECOGIDA, recogida);
        db.update(DBHelper.Tablas.CABECERA_ALBARANES, actualizar ,
                DBHelper.CabeceraAlbaranesColumnas.CODIGO_ALBARAN + "=?", idAlbaran);
    }

    public void eliminarCabeceraAlbaran(String[] idAlbaran, Context context){
        abrir();

//        db.delete(DBHelper.Tablas.DETALLE_ALBARANES,
//                DBHelper.DetalleAlbarenesColumnas.CODIGO_ALBARAN + "=?", idAlbaran);
        db.delete(DBHelper.Tablas.CABECERA_ALBARANES,
                DBHelper.CabeceraAlbaranesColumnas.CODIGO_ALBARAN + "= ?"  , idAlbaran);

        Toast.makeText(context,String.format(context.getString(R.string.albaran_eliminado),idAlbaran ),Toast.LENGTH_LONG).show();

    }



    /*
    DETALLE ALBARANES
     */

    public void nuevoDetalleAlbaran(int ultimaLinea, String codigoAlbaran){
        abrir();


        ContentValues valores = new ContentValues();
        valores.put(DBHelper.DetalleAlbarenesColumnas.CODIGO_ALBARAN, codigoAlbaran);
        valores.put(DBHelper.DetalleAlbarenesColumnas.LINEA, ultimaLinea);

        db.insert(DBHelper.Tablas.DETALLE_ALBARANES,null,valores);
    }

    public int ultimaLineaAlbaran(String codigoAlbaran){
        leer();
        String[] args = {codigoAlbaran};

        String sql = "SELECT Max(linea) As lineaMaxima From detalle_albaranes " +
                "WHERE detalle_albaranes.codigoAlbaran =?";

        Cursor c = db.rawQuery(sql,args);

        c.moveToFirst();

        int ultimoAlbaran = c.getInt(c.getColumnIndex("lineaMaxima"));

        c.close();
        cerrar();


        return ultimoAlbaran;

    }

    public static final String[] todasColumnasDetalleAlbaran = {
            DBHelper.DetalleAlbarenesColumnas.CODIGO_ALBARAN,
            DBHelper.DetalleAlbarenesColumnas.LINEA,
            DBHelper.DetalleAlbarenesColumnas.DETALLE,
            DBHelper.DetalleAlbarenesColumnas.CANTIDAD,
            DBHelper.DetalleAlbarenesColumnas.TIPO
    };

    public List<DetalleAlbaranes> verListaDetalleAlbaran(String codigoAlbaran){
        leer();
        String[] args ={codigoAlbaran};
        String criterioSeleccion = DBHelper.DetalleAlbarenesColumnas.CODIGO_ALBARAN + "=?";


        Cursor cursor = db.query(DBHelper.Tablas.DETALLE_ALBARANES,todasColumnasDetalleAlbaran,criterioSeleccion,args,null,null,DBHelper.DetalleAlbarenesColumnas.LINEA);

        List<DetalleAlbaranes> listaDetalleAlbaranes = new ArrayList<>();
        while (cursor.moveToNext()){
            DetalleAlbaranes detalles = new DetalleAlbaranes();
            detalles.setCodigoAlbaran(cursor.getString(cursor.getColumnIndex(DBHelper.DetalleAlbarenesColumnas.CODIGO_ALBARAN)));
            detalles.setLinea(cursor.getInt(cursor.getColumnIndex(DBHelper.DetalleAlbarenesColumnas.LINEA)));
            detalles.setDetalle(cursor.getString(cursor.getColumnIndex(DBHelper.DetalleAlbarenesColumnas.DETALLE)));
            detalles.setCantidad(cursor.getDouble(cursor.getColumnIndex(DBHelper.DetalleAlbarenesColumnas.CANTIDAD)));
            detalles.setTipo(cursor.getString(cursor.getColumnIndex(DBHelper.DetalleAlbarenesColumnas.TIPO)));
            listaDetalleAlbaranes.add(detalles);
        }
        cerrar();
        cursor.close();

        return listaDetalleAlbaranes;
    }


    public DetalleAlbaranes obtenerDetalleAlbaran(String codigoAlbaran, String linea){
        leer();

        String[] args ={codigoAlbaran, linea};
        String criterioSeleccion = DBHelper.DetalleAlbarenesColumnas.CODIGO_ALBARAN + "=? AND " +
                DBHelper.DetalleAlbarenesColumnas.LINEA + "=?";


        Cursor cursor = db.query(DBHelper.Tablas.DETALLE_ALBARANES,todasColumnasDetalleAlbaran,criterioSeleccion,args,null,null,null);

        DetalleAlbaranes detalleAlbaranes = new DetalleAlbaranes();
        cursor.moveToFirst();

        detalleAlbaranes.setCodigoAlbaran(codigoAlbaran);
        detalleAlbaranes.setLinea(cursor.getInt(cursor.getColumnIndex(DBHelper.DetalleAlbarenesColumnas.LINEA)));
        detalleAlbaranes.setDetalle(cursor.getString(cursor.getColumnIndex(DBHelper.DetalleAlbarenesColumnas.DETALLE)));
        detalleAlbaranes.setCantidad(cursor.getDouble(cursor.getColumnIndex(DBHelper.DetalleAlbarenesColumnas.CANTIDAD)));
        detalleAlbaranes.setTipo(cursor.getString(cursor.getColumnIndex(DBHelper.DetalleAlbarenesColumnas.TIPO)));

        cerrar();
        cursor.close();

        return  detalleAlbaranes;

    }

    public void actualizarDetalleAlbaran(String codigoAlbaran, String linea,String detalle,
                                         double cantidad, String tipo){
        abrir();

        String[] args = {codigoAlbaran,linea};

        ContentValues actualizar = new ContentValues();
        actualizar.put(DBHelper.DetalleAlbarenesColumnas.DETALLE, detalle);
        actualizar.put(DBHelper.DetalleAlbarenesColumnas.CANTIDAD, cantidad);
        actualizar.put(DBHelper.DetalleAlbarenesColumnas.TIPO, tipo);

        db.update(DBHelper.Tablas.DETALLE_ALBARANES, actualizar ,
                DBHelper.DetalleAlbarenesColumnas.CODIGO_ALBARAN + "=? AND " +
                DBHelper.DetalleAlbarenesColumnas.LINEA + "=?", args);


        cerrar();
    }

    public void eliminarDetalleAlbaran(String codigoAlbaran, String linea, Context context){
        abrir();

        String[] args = {codigoAlbaran,linea};

        db.delete(DBHelper.Tablas.DETALLE_ALBARANES,
                DBHelper.DetalleAlbarenesColumnas.CODIGO_ALBARAN + "=? AND " +
                        DBHelper.DetalleAlbarenesColumnas.LINEA + "=?", args);

        Toast.makeText(context,String.format(context.getString(R.string.detalle_eliminado), linea ),Toast.LENGTH_LONG).show();

        cerrar();

    }


}
