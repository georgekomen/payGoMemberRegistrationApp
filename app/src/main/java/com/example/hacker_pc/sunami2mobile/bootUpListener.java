package com.example.hacker_pc.sunami2mobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class bootUpListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Boot complete", Toast.LENGTH_SHORT).show();
        Intent intent1 = new Intent(context, mpesaService.class);
        context.startService(intent1);
    }
}
