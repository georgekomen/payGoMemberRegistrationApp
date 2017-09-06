package com.example.hacker_pc.sunami2mobile;

import android.content.Context;
import android.os.Handler;

import org.json.JSONArray;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Hacker-pc on 5/4/2017.
 */

public final class mpesaSync {
    final static MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    static Context ctx;
    static OkHttpClient client = new OkHttpClient();
    static private String res;

    public mpesaSync(Context ctx1) {
        ctx = ctx1;
        runnable();
    }

    static public void mpesaSync(JSONArray ja) {

        //post
        RequestBody body = RequestBody.create(JSON, ja.toString());
        Request request = new Request.Builder()
                .url("http://api.sunamiapp.net/api/customers/postReceive_mpesa/")
                .post(body)
                .build();

        //response
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                //Toast.makeText(expenses.this.getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                res = e.getMessage();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                res = response.body().string();
            }
        });
    }

    private void runnable() {
        final Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                if (res != null) {
                    constants.toast(res);
                    //constants.showAlert(ctx, res);
                }
                handler.postDelayed(this, 5000);
                res = null;
            }
        };
        handler.postDelayed(r, 2000);
    }
}
