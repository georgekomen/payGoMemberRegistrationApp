package com.example.hacker_pc.sunami2mobile;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class directions extends Fragment {
    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    View view;
    private AutoCompleteTextView client_autocomplete;
    private List<customerClass> customerList = new ArrayList<customerClass>();
    private List<String> listnames = new ArrayList<String>();

    public directions() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_directions, container, false);
        client_autocomplete = (AutoCompleteTextView) view.findViewById(R.id.client_autocomplete);
        Button dir = (Button) view.findViewById(R.id.btn_directions);
        dir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String d_name = String.valueOf(client_autocomplete.getText());
                try {
                    double d_lat = getLat1(d_name);
                    double d_lon = getLon1(d_name);
                    if (d_lat != 0) {
                        Uri uri = Uri.parse("http://maps.google.com/maps?" + "saddr="
                                + "My Location" + "&daddr=" + d_lat + "," + d_lon);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                        startActivity(intent);
                    } else {
                        constants.showCancelableAlert("customer location not yet taken");
                    }
                } catch (Exception k) {
                    constants.showCancelableAlert(k.getMessage());
                }
            }
        });
        try {
            populateCategories();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    private void populateCategories() throws JSONException {
        try {
            //get
            Request request = new Request.Builder()
                    .url("http://api.sunamiapp.net/api/customers/getCustomerLocations/")
                    .build();
            //response
            client.newCall(request).enqueue(new Callback() {
                String Customer_Id;
                Double Customer_Lat;
                Double Customer_Lon;
                boolean Customer_Status;
                String Customer_Name;

                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(directions.this.getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String res = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            JSONArray ja = null;
                            try {
                                ja = new JSONArray(res);

                                for (int n = 0; n < ja.length(); n++) {
                                    JSONObject object = ja.getJSONObject(n);

                                    //Customer_Name = object.get("Customer_Name");
                                    Customer_Name = object.getString("Customer_Name");
                                    Customer_Id = object.getString("Customer_Id");
                                    //Customer_Lat = object.get("Customer_Lat");
                                    Customer_Lat = object.getDouble("Customer_Lat");
                                    //Customer_Lon = object.get("Customer_Lon");
                                    Customer_Lon = object.getDouble("Customer_Lon");
                                    //Customer_Status = object.get("Customer_Status");
                                    Customer_Status = object.getBoolean("Customer_Status");

                                    customerClass cc = new customerClass();
                                    cc.setCustomer_Name(Customer_Name);
                                    cc.setCustomer_Id(Customer_Id);
                                    cc.setCustomer_Lat(Customer_Lat);
                                    cc.setCustomer_Lon(Customer_Lon);
                                    cc.setCustomer_Status(Customer_Status);
                                    customerList.add(cc);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                queryList();
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(directions.this.getActivity(),
                                        android.R.layout.select_dialog_item, listnames);
                                client_autocomplete.setThreshold(1);
                                client_autocomplete.setAdapter(dataAdapter);

                            } catch (Exception i) {
                                Toast.makeText(directions.this.getActivity(), i.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
        } catch (Exception l) {
            constants.toast(l.getMessage());
        }
    }

    private void queryList() {
        for (int i = 0; i < customerList.size(); i++) {
            listnames.add(customerList.get(i).getCustomer_Name());
        }
    }

    private Double getLat1(String name) {
        Double lat = 0.0;
        for (int i = 0; i < customerList.size(); i++) {
            String nm = customerList.get(i).getCustomer_Name();
            if (nm.equals(name)) {
                lat = customerList.get(i).getCustomer_Lat();
                break;
            }
        }
        return lat;
    }

    private Double getLon1(String name) {

        Double lon = 0.0;
        for (int i = 0; i < customerList.size(); i++) {
            String nm = customerList.get(i).getCustomer_Name();
            //constants.toast(nm+"\n"+name);
            if (nm.equals(name)) {
                lon = customerList.get(i).getCustomer_Lon();
                break;
            }
        }
        return lon;
    }

}
