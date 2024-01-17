package com.technogenis.carmechanics.NearByPlace;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class Utils {

    public static void ShowToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static ProgressDialog showDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing your request...");
        return progressDialog;
    }




}
