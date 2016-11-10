package at.ums.luna.liebochlieferschein.actividades;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import at.ums.luna.liebochlieferschein.R;
import at.ums.luna.liebochlieferschein.database.OperacionesBaseDatos;
import at.ums.luna.liebochlieferschein.modelos.DetalleAlbaranes;

public class FormularioAlbaranesDetalle extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView codigoAlbaran;
    private TextView linea;
    private TextView detalle;
    private TextView cantidad;
    private TextView tipo;
    private Spinner spinnerEH;
    private int changeSpinner = -1;

    private String valorCodigoAlbaran;
    private String valorLinea;

    private OperacionesBaseDatos mOperacionesBaseDatos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_albaranes_detalle);

        codigoAlbaran = (TextView) findViewById(R.id.tvCodigoAlbaran);
        linea = (TextView) findViewById(R.id.linea);
        detalle = (TextView) findViewById(R.id.detalle);
        cantidad = (TextView) findViewById(R.id.cantidad);
        tipo = (TextView) findViewById(R.id.tipo);
        spinnerEH = (Spinner) findViewById(R.id.spinnerEH);

        //llenamos el spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.eh_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEH.setAdapter(adapter);
        spinnerEH.setOnItemSelectedListener(this);

        //Obtenemos los valores a mostrar

        Intent intento = getIntent();

        Bundle bundle = intento.getExtras();
        if(bundle != null) {

            valorCodigoAlbaran = bundle.getString("codigoAlbaran");
            valorLinea = bundle.getString("linea");

        }

        mOperacionesBaseDatos = new OperacionesBaseDatos(this);

        DetalleAlbaranes detalleAlbaranes = mOperacionesBaseDatos.obtenerDetalleAlbaran(valorCodigoAlbaran, valorLinea);

        codigoAlbaran.setText(detalleAlbaranes.getCodigoAlbaran());
        linea.setText(String.valueOf(detalleAlbaranes.getLinea()));
        detalle.setText(detalleAlbaranes.getDetalle());
        cantidad.setText(String.valueOf(detalleAlbaranes.getCantidad()));
        tipo.setText(detalleAlbaranes.getTipo());


        //cierra el teclado al hacer click

        cantidad.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }

                return false;
            }
        });


        tipo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }

                return false;
            }
        });


        //Codigo para el onClickListener
        findViewById(R.id.botonCancelarDetalle).setOnClickListener(mGlobal_onClickListener);
        findViewById(R.id.botonBorrarDetalle).setOnClickListener(mGlobal_onClickListener);
        findViewById(R.id.botonActualizarDetalle).setOnClickListener(mGlobal_onClickListener);

    }

    //Intents para cualquier botón de la actividad
    final View.OnClickListener mGlobal_onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.botonCancelarDetalle:
                    finish();
                    break;
                case R.id.botonBorrarDetalle:
                    Eliminar();
                    break;
                case R.id.botonActualizarDetalle:
                    Actualizar();
                    break;
            }
        }
    };

    private void Eliminar(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(this.getString(R.string.ContinuarBorrando))
                .setTitle(this.getString(R.string.Advertencia))
                .setCancelable(false)
                .setNegativeButton(this.getString(R.string.Cancelar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton(this.getString(R.string.continuar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                mOperacionesBaseDatos.eliminarDetalleAlbaran(valorCodigoAlbaran,valorLinea, FormularioAlbaranesDetalle.this);

                                finish();

                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void Actualizar(){

        String detalleActual = detalle.getText().toString();
        double cantidadActual = Double.parseDouble(cantidad.getText().toString());
        String tipoActual = tipo.getText().toString();

        mOperacionesBaseDatos.actualizarDetalleAlbaran(valorCodigoAlbaran,valorLinea,detalleActual,
                cantidadActual,tipoActual);

        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(changeSpinner != -1) {
            String valorObtenido = (String) parent.getItemAtPosition(position);
            tipo.setText(valorObtenido);
        }
        changeSpinner = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }
}
