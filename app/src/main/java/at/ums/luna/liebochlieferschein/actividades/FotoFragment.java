package at.ums.luna.liebochlieferschein.actividades;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import at.ums.luna.liebochlieferschein.R;
import at.ums.luna.liebochlieferschein.servidor.OperacionesServidor;

/**
 * A simple {@link Fragment} subclass.
 */
public class FotoFragment extends Fragment {


    private String codigoAlbaranObtenido;
    private String nombreFoto;
    private String tempDir;
    ImageView imagen;

    private Context esteContexto;
    private static int TAKE_PICTURE = 1;
    public static final int RESULT_OK = -1;

    private File image;

    public FotoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        esteContexto = container.getContext();

        Bundle args = getArguments();
        codigoAlbaranObtenido = args.getString("codigoObtenido");
        nombreFoto = "foto" + codigoAlbaranObtenido + ".jpg";

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_foto, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tempDir = Environment.getExternalStorageDirectory() + "/" + getResources().getString(R.string.external_dir) + "/";

        imagen = (ImageView) getView().findViewById(R.id.imageFoto);


        mostrarFoto();


        ImageButton getSignature = (ImageButton) getView().findViewById(R.id.botonFoto);
        getSignature.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File imagesFolder = new File(
                        Environment.getExternalStorageDirectory(),getResources().getString(R.string.external_dir));
                imagesFolder.mkdirs();
                image = new File(imagesFolder,nombreFoto);
                Uri uriSavedImage = Uri.fromFile(image);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                startActivityForResult(intent,TAKE_PICTURE);
            }
        });



        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //desde el servidor
                tempDir = "http://77.119.243.100/upload-test/uploads/";
                String archivo = tempDir + nombreFoto;
                Picasso.with(esteContexto).load(archivo).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).error(R.drawable.leer).into(imagen);

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {


            final String ruta = image.getAbsoluteFile().toString();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    //creating new thread to handle Http Operations
                    OperacionesServidor op = new OperacionesServidor();
                    op.uploadFile(ruta,getActivity(),esteContexto);
                }
            }).start();

            mostrarFoto();

        }

    }

    public void mostrarFoto(){
        //mostrar foto con picasso desde la memoria del telefono
//        String archivo = tempDir + nombreFoto;
//
//        File fichero = new File(archivo);
//
//        Picasso.with(esteContexto).load(fichero).memoryPolicy(MemoryPolicy.NO_CACHE).error(R.drawable.leer).into(imagen);

        //desde el servidor
        tempDir = "http://77.119.243.100/upload-test/uploads/";
        String archivo = tempDir + nombreFoto;

        Picasso.with(esteContexto).load(archivo).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).error(R.drawable.leer).into(imagen);



    }


}
