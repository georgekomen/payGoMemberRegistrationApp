package com.example.hacker_pc.sunami2mobile;
//dialogs

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by Hacker-pc on 5/1/2017.
 */

public final class constants {
    public static Context context;
    static Dialog dialog;

    public constants(Context ctx) {
        context = ctx;
    }

    static public void showAlert(String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Alert");
        alert.setMessage(msg);
        alert.setCancelable(false);

        /*alert.setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });*/

        dialog = alert.create();
        dialog.show();
    }

    static public void showCancelableAlert(String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Alert");
        alert.setMessage(msg);
        alert.setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        dialog = alert.create();
        dialog.show();
    }

    static public void cancelDialog() {
        dialog.cancel();
    }

    static public void toast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
