package at.ums.luna.liebochlieferschein.actividades;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kyanogen.signatureview.SignatureView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import at.ums.luna.liebochlieferschein.R;

public class capturarFirma extends AppCompatActivity {

    SignatureView signatureView;
    private String nuevoNombreFirma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capturar_firma);

        signatureView = (SignatureView) findViewById(R.id.signature_view);


        Intent intento = getIntent();
        Bundle bundle = intento.getExtras();
        if(bundle != null){
            nuevoNombreFirma = bundle.getString("nombreFirma");
            Log.i("JUANJO", "en la firma " + nuevoNombreFirma);
        }


    }

    public void limpiar(View v){

        signatureView.clearCanvas();
    }

    public void guardarFirma(View v) {


        File imagesFolder = new File(
                Environment.getExternalStorageDirectory(),getResources().getString(R.string.external_dir));
        imagesFolder.mkdirs();
        OutputStream fOut = null;
        File file = new File(imagesFolder,nuevoNombreFirma);

        Bitmap bitmap = signatureView.getSignatureBitmap();

        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fOut);

            if (fOut != null){
                fOut.flush();
                fOut.close();

                if (bitmap !=null){
                    Toast.makeText(getApplicationContext(), R.string.firma_guardada,Toast.LENGTH_LONG).show();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finish();

    }


}
