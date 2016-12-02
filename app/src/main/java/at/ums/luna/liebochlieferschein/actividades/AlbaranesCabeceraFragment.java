package at.ums.luna.liebochlieferschein.actividades;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import at.ums.luna.liebochlieferschein.R;
import at.ums.luna.liebochlieferschein.adaptadores.DialogoListaCampoClientes;

import at.ums.luna.liebochlieferschein.database.OperacionesBaseDatos;

import at.ums.luna.liebochlieferschein.modelos.Clientes;
import at.ums.luna.liebochlieferschein.servidor.Defaults;
import at.ums.luna.liebochlieferschein.servidor.MySingleton;
import at.ums.luna.liebochlieferschein.servidor.OperacionesServidor;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlbaranesCabeceraFragment extends Fragment {

    private String codigoAlbaranObtenido;

    private OperacionesBaseDatos mOperacionesBaseDatos;
    private OperacionesServidor operacionesServidor;
    private Context esteContexto;

    TextView fecha;
    TextView idCliente;
    TextView nombreCliente;
    TextView direccionCliente;

    RadioGroup recogida1;
    RadioGroup recogida2;
    private String esRecogida;

    private EditText et;
    ListView lv;

    private int mYear;
    private int mMonth;
    private int mDay;



    public AlbaranesCabeceraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        esteContexto = container.getContext();

        Bundle args = getArguments();
        codigoAlbaranObtenido = args.getString("codigoObtenido");

        operacionesServidor = new OperacionesServidor();


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_albaranes_cabecera, container, false);



    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        refrescarDatos();

        //Codigo para el onClickListener
        getView().findViewById(R.id.botonCancelarAlbaran).setOnClickListener(mGlobal_onClickListener);
        getView().findViewById(R.id.botonBorrarAlbaran).setOnClickListener(mGlobal_onClickListener);
        getView().findViewById(R.id.botonActualizarAlbaran).setOnClickListener(mGlobal_onClickListener);
        getView().findViewById(R.id.imageButtonElegirCliente).setOnClickListener(mGlobal_onClickListener);
        getView().findViewById(R.id.tvFecha).setOnClickListener(mGlobal_onClickListener);

        getView().findViewById(R.id.radioButtonRecogida).setOnClickListener(mGlobal_onClickListener);
        getView().findViewById(R.id.radioButtonEntrega).setOnClickListener(mGlobal_onClickListener);
        getView().findViewById(R.id.radioButtonAnderes).setOnClickListener(mGlobal_onClickListener);
        getView().findViewById(R.id.radioButtonSieben).setOnClickListener(mGlobal_onClickListener);
        getView().findViewById(R.id.radioButtonSchredden).setOnClickListener(mGlobal_onClickListener);
        getView().findViewById(R.id.radioButtonUmsetzen).setOnClickListener(mGlobal_onClickListener);

    }

    //Intents para cualquier botón de la actividad
    final View.OnClickListener mGlobal_onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.botonCancelarAlbaran:
                    getActivity().finish();
                    break;
                case R.id.botonBorrarAlbaran:
                    Eliminar();
                    break;
                case R.id.botonActualizarAlbaran:
                    Actualizar();
                    getActivity().finish();
                    break;
                case R.id.imageButtonElegirCliente:
                    seleccionarCliente();
                    break;
                case R.id.tvFecha:
                    seleccionarFecha();
                    break;
                case R.id.radioButtonRecogida:
                    radioGrupoCombinado(v);
                    break;
                case R.id.radioButtonEntrega:
                    radioGrupoCombinado(v);
                    break;
                case R.id.radioButtonAnderes:
                    radioGrupoCombinado(v);
                    break;
                case R.id.radioButtonSchredden:
                    radioGrupoCombinado(v);
                    break;
                case R.id.radioButtonSieben:
                    radioGrupoCombinado(v);
                    break;
                case R.id.radioButtonUmsetzen:
                    radioGrupoCombinado(v);
                    break;
            }
        }
    };


    private void refrescarDatos() {
        mOperacionesBaseDatos = new OperacionesBaseDatos(esteContexto);


        fecha = (TextView)getView().findViewById(R.id.tvFecha);
        idCliente = (TextView)getView().findViewById(R.id.tvIdCliente);
        nombreCliente = (TextView)getView().findViewById(R.id.tvNombreCliente);
        direccionCliente = (TextView)getView().findViewById(R.id.tvDireccionCliente);
        recogida1 = (RadioGroup) getView().findViewById(R.id.radioGrupoRecogida1);
        recogida2 = (RadioGroup) getView().findViewById(R.id.radioGrupoRecogida2);

        //Obtiene los datos del servidor

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Defaults.SERVER_URL + "obtener_cabecera_albaran_por_id.php?codigoAlbaran=" + codigoAlbaranObtenido,
                (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    fecha.setText(String.valueOf(response.getString("fecha")));
                    idCliente.setText(String.valueOf(response.getInt("idCliente")));
                    nombreCliente.setText(response.getString("nombre"));
                    direccionCliente.setText(response.getString("direccion"));
                    esRecogida = response.getString("recogida");

                    switch (esRecogida){
                        case "zustellung":
                            recogida1.check(R.id.radioButtonEntrega);
                            break;
                        case "abholung":
                            recogida1.check(R.id.radioButtonRecogida);
                            break;
                        case "anderes":
                            recogida1.check(R.id.radioButtonAnderes);
                            break;
                        case "sieben":
                            recogida2.check(R.id.radioButtonSieben);
                            break;
                        case "schredden":
                            recogida2.check(R.id.radioButtonSchredden);
                            break;
                        case "umsetzen":
                            recogida2.check(R.id.radioButtonUmsetzen);
                            break;
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("JJ", error.toString());
                Toast.makeText(esteContexto, "Algo salio mal " + error,Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        });

        MySingleton.getInstance(esteContexto).addToRequestque(jsonObjectRequest);

    }

    public void radioGrupoCombinado(View v) {
        //Limpia los radioGrupos
        recogida1.clearCheck();
        recogida2.clearCheck();

        //Accion asignada manualmente
        switch (v.getId()){
            case R.id.radioButtonRecogida:
                esRecogida = "abholung";
                recogida1.check(R.id.radioButtonRecogida);
                break;
            case R.id.radioButtonEntrega:
                esRecogida = "zustellung";
                recogida1.check(R.id.radioButtonEntrega);
                break;
            case R.id.radioButtonAnderes:
                esRecogida = "anderes";
                recogida1.check(R.id.radioButtonAnderes);
                break;
            case R.id.radioButtonSieben:
                esRecogida = "sieben";
                recogida2.check(R.id.radioButtonSieben);
                break;
            case R.id.radioButtonSchredden:
                esRecogida = "schredden";
                recogida2.check(R.id.radioButtonSchredden);
                break;
            case R.id.radioButtonUmsetzen:
                esRecogida = "umsetzen";
                recogida2.check(R.id.radioButtonUmsetzen);
                break;
        }
        Actualizar();

    }


    private void Eliminar(){

        AlertDialog.Builder builder = new AlertDialog.Builder(esteContexto);
        builder.setMessage(esteContexto.getString(R.string.ContinuarBorrando))
                .setTitle(esteContexto.getString(R.string.Advertencia))
                .setCancelable(false)
                .setNegativeButton(esteContexto.getString(R.string.Cancelar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton(esteContexto.getString(R.string.continuar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                operacionesServidor.eliminarCabeceraAlbaran(esteContexto,codigoAlbaranObtenido);
                                getActivity().finish();

                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void Actualizar(){

        String [] idAlbaranActual = {codigoAlbaranObtenido};
        String fechaActual = fecha.getText().toString();
        String idClienteActual= idCliente.getText().toString();

        RadioButton rbRecogida = (RadioButton) getView().findViewById(R.id.radioButtonRecogida);

        operacionesServidor.editarCabeceraAlbaran(esteContexto,codigoAlbaranObtenido,fechaActual,idClienteActual, esRecogida);


    }

    private void seleccionarCliente(){

        final ArrayList<Clientes> listaDeClientesInicial= operacionesServidor.verListaClientesServidor1(esteContexto);


        final Dialog dialog = new Dialog(esteContexto);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogo_lista_clientes, null);

        lv = (ListView) view.findViewById(R.id.lista1);
        et = (EditText) view.findViewById(R.id.etBusqueda);

        DialogoListaCampoClientes dialogo = new DialogoListaCampoClientes(esteContexto, listaDeClientesInicial);

        lv.setAdapter(dialogo);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                idCliente.setText(String.valueOf(listaDeClientesInicial.get(position).getId()));
                nombreCliente.setText(listaDeClientesInicial.get(position).getNombre());
                direccionCliente.setText(listaDeClientesInicial.get(position).getDireccion());

                Actualizar();

                dialog.cancel();
            }
        });
        dialog.setTitle(R.string.seleccione_cliente);
        dialog.setContentView(view);
        dialog.show();


        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                final ArrayList<Clientes> listaDeClientesFiltrada = new ArrayList<Clientes>();

                for (Clientes clientes : listaDeClientesInicial) {
                    if (clientes.getNombre().contains(s)){
                        listaDeClientesFiltrada.add(clientes);
                    }
                    if (String.valueOf(clientes.getId()).contains(s)){
                        listaDeClientesFiltrada.add(clientes);
                    }
                }


                DialogoListaCampoClientes dialogoFinal = new DialogoListaCampoClientes(esteContexto, listaDeClientesFiltrada);

                lv.setAdapter(dialogoFinal);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        idCliente.setText(String.valueOf(listaDeClientesFiltrada.get(position).getId()));
                        nombreCliente.setText(listaDeClientesFiltrada.get(position).getNombre());
                        direccionCliente.setText(listaDeClientesFiltrada.get(position).getDireccion());

                        Actualizar();

                        dialog.cancel();
                    }
                });

            }
        });


    }


    private void seleccionarFecha(){


        SimpleDateFormat formatear = new SimpleDateFormat("dd.MM.yyyy");
        String fechaOriginal = fecha.getText().toString();
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(formatear.parse(fechaOriginal));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(esteContexto, mDateSetListener,
                mYear, mMonth,mDay).show();

    }


    //CODIGO DATEPICKER

    private void updateFecha(){
        fecha.setText(
                new StringBuilder()
                .append(mDay).append(".")
                .append(mMonth+1).append(".")
                .append(mYear));
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet (DatePicker view, int year,
                                       int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateFecha();
                }
            };

}
