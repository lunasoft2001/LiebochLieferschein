package at.ums.luna.liebochlieferschein.actividades;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;

import at.ums.luna.liebochlieferschein.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class firmaFragment extends Fragment {

    public static final int SIGNATURE_ACTIVITY = 1;


    private String codigoAlbaranObtenido;
    private Context esteContexto;
    private String tempDir;
    ImageView imagen;
    private String nombreFirma;

    public firmaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        esteContexto = container.getContext();

        Bundle args = getArguments();
        codigoAlbaranObtenido = args.getString("codigoObtenido");
        nombreFirma = "firma" + codigoAlbaranObtenido + ".jpg";

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_firma, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        ImageButton getSignature = (ImageButton) getView().findViewById(R.id.botonFirma);
        getSignature.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intento = new Intent(esteContexto, capturarFirma.class);
                intento.putExtra("nombreFirma", nombreFirma);
                startActivity(intento);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        tempDir = Environment.getExternalStorageDirectory() + "/" + getResources().getString(R.string.external_dir) + "/";
        imagen = (ImageView) getView().findViewById(R.id.imageFirma);
        String archivo = tempDir + nombreFirma;

        File fichero = new File(archivo);

        if(fichero.exists()){
            Bitmap bMap = BitmapFactory.decodeFile(archivo);

            imagen.setImageBitmap(bMap);
        }

    }

}
