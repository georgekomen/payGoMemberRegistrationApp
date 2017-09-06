package com.example.hacker_pc.sunami2mobile;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class expenses extends Fragment {
    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    File destination;
    private View view;
    private EditText txt_amount;
    private Spinner txt_recipient;
    private EditText txt_vendor;
    private EditText txt_dateset;
    private Spinner txt_account;
    private Spinner txt_category;
    private EditText txt_ref_code;
    private String category;
    private String amount;
    private String recipient;
    private String vendor;
    private String dateset;
    private String account;
    private String ref_code;
    private String pic1 = "";
    private byte[] picByte = null;
    private Button btn_submit;
    private Button btn_photo;
    //camera---------------------------------------------------------------------------------------------start
    private ImageView capturedImage;
    private Handler handler = new Handler();

    public expenses() {
        // Required empty public constructor
        try {
            populateCategories();
            populateUsers();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.fragment_expenses, container, false);
            // Inflate the layout for this fragment
            txt_amount = (EditText) view.findViewById(R.id.txt_pay_amount);
            txt_dateset = (EditText) view.findViewById(R.id.dateset1);
            txt_account = (Spinner) view.findViewById(R.id.txt_account);
            txt_category = (Spinner) view.findViewById(R.id.txt_description);
            txt_ref_code = (EditText) view.findViewById(R.id.txt_ref_code);
            txt_recipient = (Spinner) view.findViewById(R.id.txt_recipient);
            txt_vendor = (EditText) view.findViewById(R.id.txt_vendor);
            btn_photo = (Button) view.findViewById(R.id.button_photo);

            btn_submit = (Button) view.findViewById(R.id.button_submit);

            List<String> accounts = new ArrayList<String>();
            accounts.add("Office Mpesa");
            accounts.add("Equity Bank");
            accounts.add("Office Petty Cash");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(),
                    android.R.layout.simple_spinner_item, accounts);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            txt_account.setAdapter(dataAdapter);

            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    constants.showAlert("please wait as we record in the database");
                    category = txt_category.getSelectedItem().toString();
                    amount = txt_amount.getText().toString();
                    recipient = txt_recipient.getSelectedItem().toString();
                    dateset = txt_dateset.getText().toString();
                    account = txt_account.getSelectedItem().toString();
                    ref_code = txt_ref_code.getText().toString();
                    vendor = txt_vendor.getText().toString();

                    JSONArray ja = new JSONArray();
                    JSONObject jo = new JSONObject();
                    try {
                        jo.put("category", category);
                        jo.put("amount", amount);
                        jo.put("recipient", recipient);
                        jo.put("dateset", dateset);
                        jo.put("account", account);
                        jo.put("ref_code", ref_code);
                        jo.put("vendor", vendor);
                        jo.put("pic1", pic1);
                        //jo.put("picByte", picByte);
                        //get variable from main activity
                        jo.put("recordedBy", ((MainActivity) getActivity()).userEmail);
                        ja.put(jo);
                    } catch (JSONException k) {
                        //Toast.makeText(expenses.this.getActivity(), k.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    OkHttpClient client = new OkHttpClient();

                    //post
                    RequestBody body = RequestBody.create(JSON, ja.toString());
                    Request request = new Request.Builder()
                            .url("http://api.sunamiapp.net/api/customers/postExpense/")
                            .post(body)
                            .build();

                    //get
                /*Request request = new Request.Builder()
                        .url("http://api.sunamiapp.net/api/customers/getFreeImei/")
                        .build();*/

                    //response
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            constants.cancelDialog();
                            constants.showCancelableAlert("error while recording");
                            //Toast.makeText(expenses.this.getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String res = response.body().string();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    constants.cancelDialog();
                                    constants.showCancelableAlert(res);
                                    //Toast.makeText(expenses.this.getActivity(), res, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });

                }

            });

            txt_dateset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowDateDialog();
                }
            });

            btn_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //show date dialog here
                    ShowCameraDialog();
                }
            });
            runnable();
            return view;
        } catch (Exception h) {
            Toast.makeText(expenses.this.getActivity(), h.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }

    private void populateCategories() throws JSONException {
        //get
        Request request = new Request.Builder()
                .url("http://api.sunamiapp.net/api/customers/getExpenseCategories/")
                .build();
        //response
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(expenses.this.getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<String> li = new ArrayList<String>();
                        JSONArray ja = null;
                        try {
                            ja = new JSONArray(res);
                            for (int n = 0; n < ja.length(); n++) {
                                JSONObject object = ja.getJSONObject(n);
                                Object level = object.get("Name");
                                li.add(level.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(expenses.this.getActivity(),
                                    android.R.layout.simple_spinner_item, li);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            txt_category.setAdapter(dataAdapter);
                        } catch (Exception i) {
                            Toast.makeText(expenses.this.getActivity(), i.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private void populateUsers() throws JSONException {
        //get
        Request request = new Request.Builder()
                .url("http://api.sunamiapp.net/api/customers/getUserNames/")
                .build();
        //response
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(expenses.this.getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<String> li = new ArrayList<String>();
                        JSONArray ja = null;
                        try {
                            ja = new JSONArray(res);
                            for (int n = 0; n < ja.length(); n++) {
                                JSONObject object = ja.getJSONObject(n);
                                Object level = object.get("email");
                                li.add(level.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(expenses.this.getActivity(),
                                    android.R.layout.simple_spinner_item, li);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            txt_recipient.setAdapter(dataAdapter);
                        } catch (Exception i) {
                            Toast.makeText(expenses.this.getActivity(), i.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private void ShowCameraDialog() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this.getActivity());
        View promptsView = li.inflate(R.layout.picture_preview, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this.getActivity());
        // set prompts.xml to alertdialog builder
        openCamera();
        alertDialogBuilder.setView(promptsView);
        //populate email autocomplete

        capturedImage = (ImageView) promptsView.findViewById(R.id.capturedImage);

        // set dialog message
        alertDialogBuilder.setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        alertDialogBuilder.setCancelable(false).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        // create alert dialog
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
    //camera---------------------------------------------------------------------------------------------end

    private void openCamera() {
        //thumbnail
        /*Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);*/

        destination = new File(Environment.getExternalStorageDirectory(), "image.jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        //thumbnail
        /*if (resultCode == this.getActivity().RESULT_OK) {
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            capturedImage.setImageBitmap(bp);
            pic1 = getEncodeData(bp);
        }*/

        if (resultCode == this.getActivity().RESULT_OK) {
            try {
                FileInputStream in = new FileInputStream(destination);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 7;
                Bitmap user_picture_bmp = BitmapFactory.decodeStream(in, null, options);
                capturedImage.setImageBitmap(user_picture_bmp);
                pic1 = getEncodeData(user_picture_bmp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //convert bitmap to string
    private String getEncodeData(Bitmap bm) {
        String encodedimage1 = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 50, baos); //bm is the bitmap object

            picByte = baos.toByteArray();
            encodedimage1 = Base64.encodeToString(picByte, Base64.DEFAULT);
        } catch (Exception e) {
            System.out
                    .println("Exception: In getEncodeData" + e.toString());
        }
        return encodedimage1;
    }

    private void ShowDateDialog() {
        datePickerClass.ShowDateDialog(this.getActivity());
    }

    private void runnable() {
        Runnable r = new Runnable() {
            public void run() {
                txt_dateset.setText(datePickerClass.getDateset2());
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(r, 2000);
    }
}
