package at.ums.luna.liebochlieferschein.modelos;

/**
 * Created by luna-aleixos on 30.05.2016.
 */
public class Trabajador {

    private int _id;
    private String id;
    private String nombre;

    public Trabajador(int _id, String id, String nombre){
        this._id = _id;
        this.id = id;
        this.nombre = nombre;
    }

    public Trabajador(){

    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
