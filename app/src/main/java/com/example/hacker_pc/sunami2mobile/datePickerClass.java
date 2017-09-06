package com.example.hacker_pc.sunami2mobile;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Hacker-pc on 5/11/2017.
 */

public final class datePickerClass {
    static final int DATE_DIALOG_ID = 999;
    private static int year;
    private static int month;
    private static int day;
    private static DatePicker txt_date;
    private static String dateset2;

    public static void ShowDateDialog(Context ctx1) {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(ctx1);
        View promptsView = li.inflate(R.layout.custom_date_picker, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ctx1);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        //populate email autocomplete
        txt_date = (DatePicker) promptsView.findViewById(R.id.datePicker1);
        setCurrentDateOnView();
        // set dialog message
        alertDialogBuilder.setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                day = txt_date.getDayOfMonth();
                month = txt_date.getMonth() + 1;
                year = txt_date.getYear();
                dateset2 = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
            }
        });

        alertDialogBuilder.setCancelable(false).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dateset2 = "";
            }
        });
        // create alert dialog
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
    //datepicker---------------------------------------------------------------------------------------end

    // display current date
    public static void setCurrentDateOnView() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        // set current date into textview
        dateset2 = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
        // set current date into datepicker
        try {
            txt_date.init(year, month, day, null);
        } catch (Exception k) {

        }
    }

    public static String getDateset2() {
        return dateset2;
    }

    public void setDateset2(String dateset2) {
        datePickerClass.dateset2 = dateset2;
    }
}
