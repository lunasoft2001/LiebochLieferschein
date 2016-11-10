package at.ums.luna.liebochlieferschein.modelos;

/**
 * Created by luna-aleixos on 30.05.2016.
 */
public class DetalleAlbaranes {

    private String codigoAlbaran;
    private int linea;
    private String detalle;
    private double cantidad;
    private String tipo;

    public  DetalleAlbaranes(String codigoAlbaran, int linea, String detalle, double cantidad, String tipo){
        this.codigoAlbaran = codigoAlbaran;
        this.linea = linea;
        this.detalle = detalle;
        this.cantidad = cantidad;
        this.tipo = tipo;
    }

    public DetalleAlbaranes(){

    }

    public String getCodigoAlbaran() {
        return codigoAlbaran;
    }

    public void setCodigoAlbaran(String codigoAlbaran) {
        this.codigoAlbaran = codigoAlbaran;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
