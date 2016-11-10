package at.ums.luna.liebochlieferschein.modelos;

import java.util.List;

/**
 * Created by luna-aleixos on 14.06.2016.
 */
public class AlbaranCompleto {

    public int id;
    public String fecha;
    public int idCliente;
    public String idTrabajador;
    public String codigoAlbaran;
    public String recogida;

    public String nombreCliente;
    public String direccionCliente;
    public String telefonoCliente;
    public String emailCliente;

    public List<DetalleAlbaranes> listaDetallesAlbaran;


    public AlbaranCompleto(int id, String fecha, int idCliente, String idTrabajador,
                             String codigoAlbaran, String nombreCliente,
                             String direccionCliente, String telefonoCliente, String emailCliente, String recogida, List <DetalleAlbaranes> listaDetallesAlbaran){
        this.id = id;
        this.fecha = fecha;
        this.idCliente = idCliente;
        this.idTrabajador = idTrabajador;
        this.codigoAlbaran = codigoAlbaran;
        this.recogida = recogida;
        this.nombreCliente = nombreCliente;
        this.direccionCliente = direccionCliente;
        this.telefonoCliente = telefonoCliente;
        this.emailCliente = emailCliente;
        this.listaDetallesAlbaran = listaDetallesAlbaran;
    }

    public  AlbaranCompleto(){

    }

}
