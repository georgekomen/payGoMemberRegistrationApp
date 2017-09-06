package com.example.hacker_pc.sunami2mobile;


import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
public class register extends Fragment implements LocationListener {
    private static LocationManager locMgr;
    private static Handler handler = new Handler();
    private static Runnable r;
    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    private EditText id;
    private EditText name;
    private EditText number;
    private EditText number2;
    private EditText number3;
    private EditText box;
    private EditText occupation;
    private EditText witness;
    private EditText witnessid;
    private EditText village;
    private EditText city;
    private EditText description;
    private Button button;
    private View view;
    private String idf;
    private String namef;
    private String numberf;
    private String numberf2;
    private String numberf3;
    private String boxf;
    private String occupationf;
    private String witnessf;
    private String witnessidf;
    private String villagef;
    private String cityf;
    private String descriptionf;
    private Location locationGPS;
    private boolean cancelledDialog = false;
    private boolean cancelledDialog2 = false;

    public register() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.fragment_register, container, false);
            // Inflate the layout for this fragment
            button = (Button) view.findViewById(R.id.btn_save);
            id = (EditText) view.findViewById(R.id.customer_id);
            name = (EditText) view.findViewById(R.id.name);
            number = (EditText) view.findViewById(R.id.number);
            number2 = (EditText) view.findViewById(R.id.number2);
            number3 = (EditText) view.findViewById(R.id.number3);
            box = (EditText) view.findViewById(R.id.po_box);
            occupation = (EditText) view.findViewById(R.id.occupation);
            witness = (EditText) view.findViewById(R.id.witness);
            witnessid = (EditText) view.findViewById(R.id.witness_id);
            village = (EditText) view.findViewById(R.id.village);
            city = (EditText) view.findViewById(R.id.city);
            description = (EditText) view.findViewById(R.id.description);
            getLoc();
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //get phone imei
                    TelephonyManager tm = (TelephonyManager) register.this.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                    String imei = tm.getDeviceId();
                    constants.showAlert("please wait as we record in the database");

                    idf = id.getText().toString();
                    namef = name.getText().toString();
                    numberf = number.getText().toString();
                    numberf2 = number2.getText().toString();
                    numberf3 = number3.getText().toString();
                    boxf = box.getText().toString();
                    occupationf = occupation.getText().toString();
                    witnessf = witness.getText().toString();
                    witnessidf = witnessid.getText().toString();
                    villagef = village.getText().toString();
                    cityf = city.getText().toString();
                    descriptionf = description.getText().toString();

                    JSONArray ja = new JSONArray();
                    JSONObject jo = new JSONObject();
                    try {
                        jo.put("latG", String.valueOf(locationGPS.getLatitude()));
                        jo.put("lonG", String.valueOf(locationGPS.getLongitude()));
                        jo.put("number3", numberf3);
                        jo.put("number2", numberf2);
                        jo.put("number1", numberf);
                        jo.put("id", idf);
                        jo.put("name", namef);
                        jo.put("box", boxf);
                        jo.put("occupation", occupationf);
                        jo.put("witness", witnessf);
                        jo.put("witnessid", witnessidf);
                        jo.put("village", villagef);
                        jo.put("city", cityf);
                        jo.put("description", descriptionf);

                        //get mainactivity variable from fragment
                        jo.put("recordedBy", ((MainActivity) getActivity()).userEmail);
                        jo.put("location", address());
                        //yyyy-mm-dd e.g. 2017-04-05 - date1
                        datePickerClass.setCurrentDateOnView();
                        jo.put("date1", datePickerClass.getDateset2());
                        //Toast.makeText(register.this.getActivity(),String.valueOf(jo), Toast.LENGTH_LONG).show();
                        ja.put(jo);
                    } catch (JSONException k) {
                        //Toast.makeText(expenses.this.getActivity(), k.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    //post
                    RequestBody body = RequestBody.create(JSON, ja.toString());
                    Request request = new Request.Builder()
                            .url("http://api.sunamiapp.net/api/customers/postNewCustomer/")
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
            return view;
        } catch (Exception k) {
            Toast.makeText(register.this.getActivity(), k.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }

    private void getLoc() {
        //threading with runnable
        r = new Runnable() {
            public void run() {
                locMgr = (LocationManager) register.this.getActivity().getSystemService(register.this.getActivity().LOCATION_SERVICE);
                if (locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    try {
                        if (cancelledDialog) {
                            constants.cancelDialog();
                            cancelledDialog = false;
                        }
                        if (ActivityCompat.checkSelfPermission(register.this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(register.this.getActivity(),
                                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        locMgr.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER, 5000, 10, register.this);
                        locationGPS = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        locationGPS.setAccuracy(3);//high accuracy
                        ToastMessage(String.valueOf(" Lat = " + locationGPS.getLatitude() + " Lon = " + locationGPS.getLongitude() + " Address = " + address()));
                    } catch (Exception k) {
                        if (!cancelledDialog2) {
                            //Toast.makeText(register.this.getActivity(), k.getMessage(), Toast.LENGTH_LONG).show();
                            constants.showCancelableAlert("Make sure you have internet connection");
                            cancelledDialog2 = true;
                        }
                    }
                } else if (!locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    try {
                        if (!cancelledDialog) {
                            constants.showAlert("please enable gps first");
                            cancelledDialog = true;
                        }
                        //return;
                    } catch (Exception k) {
                        Toast.makeText(register.this.getActivity(), k.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                handler.postDelayed(this, 6000);
            }
        };
        handler.postDelayed(r, 1000);


        /*try {
            handler.postDelayed(new Runnable() {
                public void run() {
                }
            }, 1000);
        }
        catch (Exception l){
            Toast.makeText(register.this.getActivity(), l.getMessage(), Toast.LENGTH_LONG).show();
        }*/


        //multi threading
        /*Thread myThread = new Thread(new Runnable() {
            @Override
            public void run() {
               handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        //action
                    }
                });
            }
        });
        myThread.start();*/

    }

    private String address() {
        /*------- To get city name from coordinates -------- */
        String cityName = null;
        Geocoder gcd = new Geocoder(register.this.getActivity(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(locationGPS.getLatitude(),
                    locationGPS.getLongitude(), 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }

    public void ToastMessage(String msg) {
        try {
            Toast toast;
            toast = Toast.makeText(this.getActivity(), "", Toast.LENGTH_LONG);
            toast.setText(msg);
            toast.setGravity(Gravity.TOP, 0, 0);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            toastLayout.setBackgroundColor(Color.RED);
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(10);
            toast.show();
        } catch (Exception k) {
            Toast.makeText(register.this.getActivity(), k.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(r);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.post(r);
    }

}
