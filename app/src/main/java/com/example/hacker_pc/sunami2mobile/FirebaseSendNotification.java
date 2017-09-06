package com.example.hacker_pc.sunami2mobile;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Hacker-pc on 5/1/2017.
 */

public final class FirebaseSendNotification {
    static MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    static OkHttpClient client = new OkHttpClient();
    static Handler handler = new Handler();
    static String res;
    static String token = FirebaseInstanceId.getInstance().getToken();
    static Context ctx;

    public FirebaseSendNotification(Context ctx1) {
        ctx = ctx1;
        runnable();
    }

    static public void postMsg(Context ctx, JSONObject jo1) {
        JSONObject jo2 = new JSONObject();
        try {
            jo2.put("to", token);
            jo2.put("notification", jo1);
            Send(ctx, jo2.toString());
        } catch (JSONException e) {
        }
    }

    static public void Send(final Context ctx, String ja) {
        //post
        RequestBody body = RequestBody.create(JSON, ja);
        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "key=AIzaSyDMc-OxeU9BSTqi-m2L_qQeYa3dEtgRbkw")
                .build();

        //get
        /*Request request = new Request.Builder()
                .url("http://api.sunamiapp.net/api/customers/getFreeImei/")
                .build();*/

        //response
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //Toast.makeText(ctx, "post failled", Toast.LENGTH_LONG).show();
                res = "post failled";
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                res = response.body().string();
                //Toast.makeText(ctx, res, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void runnable() {
        Runnable r = new Runnable() {
            public void run() {
                if (res != null) {
                    Toast.makeText(ctx, res, Toast.LENGTH_LONG).show();
                    constants.showAlert(res);
                }
                handler.postDelayed(this, 2000);
                res = null;
            }
        };
        handler.postDelayed(r, 1000);
    }

}