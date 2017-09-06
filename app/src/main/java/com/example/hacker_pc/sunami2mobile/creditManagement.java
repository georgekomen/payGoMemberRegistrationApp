package com.example.hacker_pc.sunami2mobile;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
public class creditManagement extends Fragment {

    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    private View view;
    private ListView lv;
    private List<systemControllerClass> sunamiController = new ArrayList<systemControllerClass>();
    private List<String> listnumbers = new ArrayList<String>();

    public creditManagement() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_credit_management, container, false);
        lv = (ListView) view.findViewById(R.id.listView1);
        try {
            populateCategories();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String value = lv.getItemAtPosition(i).toString();
                Toast.makeText(creditManagement.this.getActivity(), value, Toast.LENGTH_LONG).show();
                value = value.substring(4, value.length());
                value = "tel:*140*10*0" + value + "#";
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(value));
                startActivity(intent);
            }
        });
        return view;
    }

    private void populateCategories() throws JSONException {
        //get
        Request request = new Request.Builder()
                .url("http://api.sunamiapp.net/api/customers/getSunamiControllers/")
                .build();
        //response
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(creditManagement.this.getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
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

                                String Imei = object.get("Imei").toString();
                                String Sim_Number = object.get("Sim_Number").toString();
                                String Provider = object.get("Provider").toString();
                                String Version = object.get("Version").toString();
                                String Reg_Date = object.get("Reg_Date").toString();
                                String Recorded_By = object.get("Recorded_By").toString();

                                systemControllerClass sc = new systemControllerClass();
                                sc.setImei(Imei);
                                sc.setSim_Number(Sim_Number);
                                sc.setProvider(Provider);
                                sc.setVersion(Version);
                                sc.setReg_Date(Reg_Date);
                                sc.setRecorded_By(Recorded_By);

                                sunamiController.add(sc);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            queryList();
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(creditManagement.this.getActivity(),
                                    android.R.layout.simple_list_item_1, listnumbers);
                            lv.setAdapter(adapter);
                        } catch (Exception i) {
                            Toast.makeText(creditManagement.this.getActivity(), i.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private void queryList() {
        for (int i = 0; i < sunamiController.size(); i++) {
            listnumbers.add(sunamiController.get(i).getSim_Number());
        }
    }


}
