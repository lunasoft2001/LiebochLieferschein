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

import java.io.File;
import java.io.IOException;

import at.ums.luna.liebochlieferschein.R;

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
        String archivo = tempDir + nombreFoto;
        File fichero = new File(archivo);

        if(fichero.exists()){
            Bitmap bMap = BitmapFactory.decodeFile(archivo);
            imagen.setImageBitmap(bMap);

            rodarFoto(bMap);
        }


        ImageButton getSignature = (ImageButton) getView().findViewById(R.id.botonFoto);
        getSignature.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File imagesFolder = new File(
                        Environment.getExternalStorageDirectory(),getResources().getString(R.string.external_dir));
                imagesFolder.mkdirs();
                File image = new File(imagesFolder,nombreFoto);
                Uri uriSavedImage = Uri.fromFile(image);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                startActivityForResult(intent,TAKE_PICTURE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {


            //Metodo inicial
            Bitmap bMap = BitmapFactory.decodeFile(
                    tempDir + nombreFoto);
            imagen.setImageBitmap(bMap);


            rodarFoto(bMap);

        }

    }

    public void rodarFoto(Bitmap bMap){

        String archivo = tempDir + nombreFoto;
        File imageFile = new File(archivo);

        try {
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
            int rotate = 0;
            switch (orientation){
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate-=90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate=180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate=90;
                    break;
            }

            Log.i("JUANJO","orientation = " + orientation + " - rotate = " + rotate);
            Matrix matrix = new Matrix();
            matrix.postRotate(rotate);

            Bitmap nuevoBitmap = Bitmap.createBitmap(bMap,0,0,bMap.getWidth(),bMap.getHeight(),matrix,true);

            imagen.setImageBitmap(nuevoBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
