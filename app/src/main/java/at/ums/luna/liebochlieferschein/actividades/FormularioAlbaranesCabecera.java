package at.ums.luna.liebochlieferschein.actividades;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.itextpdf.text.DocumentException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import at.ums.luna.liebochlieferschein.R;
import at.ums.luna.liebochlieferschein.adaptadores.PdfManager;
import at.ums.luna.liebochlieferschein.database.OperacionesBaseDatos;
import at.ums.luna.liebochlieferschein.modelos.AlbaranCompleto;
import at.ums.luna.liebochlieferschein.modelos.CabeceraAlbaranes;
import at.ums.luna.liebochlieferschein.servidor.Defaults;
import at.ums.luna.liebochlieferschein.servidor.MySingleton;
import at.ums.luna.liebochlieferschein.servidor.OperacionesServidor;

public class FormularioAlbaranesCabecera extends FragmentActivity {

    OperacionesServidor mOperacionesServidor;

    private String codigoAlbaranObtenido;
    private TextView numeroAlbaran;
    ImageButton create_pdf;
    ImageButton read_pdf;
    ImageButton send_email_pdf;
    Activity activity;


    AlbaranCompleto invoiceObject = new AlbaranCompleto();
    private String INVOICES_FOLDER = "lieferschein";
    private String FILENAME;

    private PdfManager pdfManager = null;

    private ProgressDialog dialogo;
    private String documentoPDF;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_albaranes_cabecera);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new AdaptadorPager(getSupportFragmentManager()));

        //Obtener el codigoCabecera
        Intent intento = getIntent();
        Bundle bundle = intento.getExtras();
        if (bundle != null) {
            codigoAlbaranObtenido = bundle.getString("codigoAlbaran");
        }

        numeroAlbaran = (TextView) findViewById(R.id.tvDetalle);
        numeroAlbaran.setText(codigoAlbaranObtenido);

        FILENAME = codigoAlbaranObtenido + ".pdf";

        dialogo = new ProgressDialog(this);
        activity= this;

        documentoPDF = "";

        mOperacionesServidor = new OperacionesServidor();

        //Creamos un albaran desde nuestro código solo para poder generar el documento PDF con esta información

        try {
            //Instanciamos la clase PdfManager
            pdfManager = new PdfManager(FormularioAlbaranesCabecera.this);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        create_pdf = (ImageButton) findViewById(R.id.botonCrearPDF);
        read_pdf = (ImageButton) findViewById(R.id.botonVerPDF);
        send_email_pdf = (ImageButton) findViewById(R.id.botonEnviarPDF);

        visionBotonesPDF();


        create_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Create PDF document
                new obtenerIvoiceObjectAsync().execute();

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //creating new thread to handle Http Operations
//                        OperacionesServidor op = new OperacionesServidor();
//                        op.uploadFile(documentoPDF,activity,FormularioAlbaranesCabecera.this);
//                    }
//                }).start();

            }
        });

        read_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert pdfManager != null;
                pdfManager.showPdfFile(INVOICES_FOLDER + File.separator + FILENAME, FormularioAlbaranesCabecera.this);
            }
        });

        send_email_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new enviarEmailAsync().execute();

            }
        });

    }


    private class enviarEmailAsync extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            dialogo.setMessage("Sending PDF");
            dialogo.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            createInvoiceObject();

            while (invoiceObject.codigoAlbaran == null){}

//            while (invoiceObject.listaDetallesAlbaran.size() == 0){ }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            String emailTo = invoiceObject.emailCliente;
            String emailCC = "juanjolunabowling@gmail.com";
            assert pdfManager != null;

            pdfManager.sendPdfByEmail(INVOICES_FOLDER + File.separator + FILENAME, emailTo, emailCC, FormularioAlbaranesCabecera.this);

            dialogo.dismiss();

        }

    }

    private class obtenerIvoiceObjectAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            dialogo.setMessage("Creating PDF");
            dialogo.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            createInvoiceObject();

            assert pdfManager != null;

            while (invoiceObject.codigoAlbaran == null){}

            while (invoiceObject.listaDetallesAlbaran.size() == 0){ }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (invoiceObject.listaDetallesAlbaran.get(0).getDetalle().equals("error")){
                Toast.makeText(FormularioAlbaranesCabecera.this,"ACHTUNG: DETAIL IST LEER...",Toast.LENGTH_LONG).show();
            }

            documentoPDF = pdfManager.createPdfDocument(invoiceObject, codigoAlbaranObtenido);
            read_pdf.setVisibility(View.VISIBLE);
            send_email_pdf.setVisibility(View.VISIBLE);

            dialogo.dismiss();

            Log.i("JJ", " el PDF es: "+  documentoPDF);

        }
    }

    private class AdaptadorPager extends FragmentPagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.CONTENIDOS);
                case 1:
                    return getString(R.string.DETALLE);
                case 2:
                    return getString(R.string.IMAGEN);
                case 3:
                    return getString(R.string.FIRMA);
                default:
                    return "---";
            }
        }

        public AdaptadorPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Bundle args = new Bundle();
            args.putString("codigoObtenido", codigoAlbaranObtenido);


            switch (position) {
                case 0:
                    AlbaranesCabeceraFragment f1 = new AlbaranesCabeceraFragment();
                    f1.setArguments(args);
                    return f1;
                case 1:
                    DetalleAlbaranFragment f2 = new DetalleAlbaranFragment();
                    f2.setArguments(args);
                    return f2;
                case 2:
                    FotoFragment f3 = new FotoFragment();
                    f3.setArguments(args);
                    return f3;
                case 3:
                    firmaFragment f4 = new firmaFragment();
                    f4.setArguments(args);
                    return f4;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, R.string.albaran_cerrado, Toast.LENGTH_SHORT).show();
        finish();
    }


    //Crea el documento importando los datos
    private void createInvoiceObject() {

        CabeceraAlbaranes albaran = mOperacionesServidor.obtenerAlbaranCompleto(this, codigoAlbaranObtenido);
        while (albaran.getCodigoAlbaran()==null){}

        invoiceObject.codigoAlbaran = albaran.getCodigoAlbaran();
        invoiceObject.fecha = albaran.getFecha();
        invoiceObject.idCliente = albaran.getIdCliente();
        invoiceObject.nombreCliente = albaran.getNombreCliente();
        invoiceObject.direccionCliente = albaran.getDireccionCliente();
        invoiceObject.telefonoCliente = albaran.getTelefonoCliente();
        invoiceObject.emailCliente = albaran.getEmailCliente();
        invoiceObject.recogida = albaran.getRecogida();

        invoiceObject.listaDetallesAlbaran = mOperacionesServidor.verListaDetalleAlbaran(this, codigoAlbaranObtenido);


    }

    public void visionBotonesPDF() {

        String tempDir = Environment.getExternalStorageDirectory() + "/" + getResources().getString(R.string.external_dir) + "/";
        String archivo = tempDir + INVOICES_FOLDER + File.separator + FILENAME;
        File fichero = new File(archivo);

        if (fichero.exists()) {
            read_pdf.setVisibility(View.VISIBLE);
            send_email_pdf.setVisibility(View.VISIBLE);
        }

    }

}
