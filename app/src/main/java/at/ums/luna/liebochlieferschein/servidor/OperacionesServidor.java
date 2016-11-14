package at.ums.luna.liebochlieferschein.servidor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.ums.luna.liebochlieferschein.database.DBHelper;
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
//    public static final String[] todasColumnasClientes = {
//            DBHelper.Clientes.ID,
//            DBHelper.Clientes.NOMBRE,
//            DBHelper.Clientes.DIRECCION,
//            DBHelper.Clientes.TELEFONO,
//            DBHelper.Clientes.EMAIL
//    };

    public List<Clientes> verListaClientesServidor(Context context){
        BackgroundTask backgroundTask = new BackgroundTask(context);
        List<Clientes> listaClientes = backgroundTask.getListaClientes();

        return listaClientes;
    }


    /**
     * ALBARANES CABECERA
     */

    public List<CabeceraAlbaranes> verListaAlbaranesCabeceraCompleta(Context context){
        BackgroundTask backgroundTask = new BackgroundTask(context);
        List<CabeceraAlbaranes> listaAlbaranes = backgroundTask.getListaAlbaranesCabecera();

        return listaAlbaranes;
    }

    public void nuevaCabeceraAlbaran(Context context, int ultimoAlbaran, String idTrabajador){

        int nuevoAlbaran = ultimoAlbaran + 1;
        String nuevoCodigoAlbaran = idTrabajador + String.valueOf(nuevoAlbaran);
        Log.i("JJ", "en operaciones servidor");

        BackgroundTask backgroundTask = new BackgroundTask(context);
        backgroundTask.nuevaCabeceraAlbaran(context,String.valueOf(nuevoAlbaran),idTrabajador,nuevoCodigoAlbaran,
                obtenerFechaActual(),"1","abholung");

    }

    private String obtenerFechaActual() {

        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd.MM.yyyy");
        return formateador.format(ahora);
    }

}
