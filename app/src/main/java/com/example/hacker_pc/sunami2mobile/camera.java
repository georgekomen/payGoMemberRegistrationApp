package com.example.hacker_pc.sunami2mobile;
//not used in this project -- only for reference

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;


/**
 * A simple {@link Fragment} subclass.
 */

public class camera extends Fragment {
    private Button btnCamera;
    private ImageView capturedImage;
    private View view;

    public camera() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_camera, container, false);
        Typeface font = Typeface.createFromAsset(this.getActivity().getAssets(), "fontawesome-webfont.ttf");
        btnCamera = (Button) view.findViewById(R.id.btnCamera);

        capturedImage = (ImageView) view.findViewById(R.id.capturedImage);

        btnCamera.setTypeface(font);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        return view;
    }

    private void openCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.getActivity().RESULT_OK) {
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            capturedImage.setImageBitmap(bp);
        }
    }

    //get string from bitmap
    private String getEncodeData(String filePath) {
        String encodedimage1 = null;
        if (filePath != null && filePath.length() > 0) {
            try {
                Bitmap bm = decodeFile(new File(filePath));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 50, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                encodedimage1 = Base64.encodeToString(b, Base64.DEFAULT);
            } catch (Exception e) {
                System.out
                        .println("Exception: In getEncodeData" + e.toString());
            }
        }
        return encodedimage1;
    }

    //get bitmap from file path
    private Bitmap decodeFile(File f) {
        Bitmap b = null;
        final int IMAGE_MAX_SIZE = 100;
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2.0, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (Exception e) {

        }
        return b;
    }

}
