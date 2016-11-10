package at.ums.luna.liebochlieferschein.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by luna-aleixos on 24.05.2016.
 */


public class DBHelper extends SQLiteOpenHelper{
    private static int version = 1;
    private static String nombreDb = "liebochLieferschein.db";
    private static SQLiteDatabase.CursorFactory factory = null;

    public DBHelper (Context context){
        super(context, nombreDb,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(this.getClass().toString(), "Creando base de datos");

            /**
             * Creamos la tabla clientes
             */
            db.execSQL("CREATE TABLE "  + Tablas.CLIENTES +
                    "( " +
                    Clientes.ID +" INT PRIMARY KEY," +
                    Clientes.NOMBRE + " TEXT NOT NULL," +
                    Clientes.DIRECCION + " TEXT," +
                    Clientes.TELEFONO +" TEXT," +
                    Clientes.EMAIL + " TEXT)");
            Log.i(this.getClass().toString(), "La tabla " + Tablas.CLIENTES + " ha sido creada");

        /**
         * Creamos la tabla cabecera_albaranes
         */
        db.execSQL("CREATE TABLE "  + Tablas.CABECERA_ALBARANES +
                "( " +
                CabeceraAlbaranesColumnas.ID +" INT NOT NULL," +
                CabeceraAlbaranesColumnas.FECHA +" TEXT NOT NULL," +
                CabeceraAlbaranesColumnas.ID_CLIENTE +" INT NOT NULL," +
                CabeceraAlbaranesColumnas.ID_TRABAJADOR +" TEXT NOT NULL," +
                CabeceraAlbaranesColumnas.CODIGO_ALBARAN +" TEXT NOT NULL PRIMARY KEY," +
                CabeceraAlbaranesColumnas.RECOGIDA + " TEXT)");
        Log.i(this.getClass().toString(), "La tabla " + Tablas.CABECERA_ALBARANES + " ha sido creada");

        /**
         * Creamos la tabla detalle_albaranes
         */
        db.execSQL("CREATE TABLE "  + Tablas.DETALLE_ALBARANES +
                "( " +
                DetalleAlbarenesColumnas.CODIGO_ALBARAN +" TEXT NOT NULL," +
                DetalleAlbarenesColumnas.LINEA +" INT NOT NULL," +
                DetalleAlbarenesColumnas.DETALLE +" TEXT," +
                DetalleAlbarenesColumnas.CANTIDAD +" DOUBLE," +
                DetalleAlbarenesColumnas.TIPO +" TEXT)");
        Log.i(this.getClass().toString(), "La tabla " + Tablas.DETALLE_ALBARANES + " ha sido creada");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + Tablas.CLIENTES);
            db.execSQL("DROP TABLE IF EXISTS " + Tablas.CABECERA_ALBARANES);
            db.execSQL("DROP TABLE IF EXISTS " + Tablas.DETALLE_ALBARANES);

            onCreate(db);

        }

    /**
     * Interfaces de las tablas para facilitar su entrada
     */

    public interface Tablas{
        String CABECERA_ALBARANES = "cabecera_albaranes";
        String DETALLE_ALBARANES = "detalle_albaranes";
        String CLIENTES = "clientes";
    }

    public interface CabeceraAlbaranesColumnas {
        String ID = "id";
        String FECHA = "fecha";
        String ID_CLIENTE = "idCliente";
        String ID_TRABAJADOR = "idTrabajador";
        String CODIGO_ALBARAN = "codigoAlbaran";
        String RECOGIDA = "recogida";
    }

    public interface DetalleAlbarenesColumnas{
        String CODIGO_ALBARAN = "codigoAlbaran";
        String LINEA = "linea";
        String DETALLE = "detalle";
        String CANTIDAD = "cantidad";
        String TIPO = "tipo";
    }

    public interface Clientes{
        String ID = "id";
        String NOMBRE = "nombre";
        String DIRECCION = "direccion";
        String TELEFONO = "telefono";
        String EMAIL = "email";
    }


}
