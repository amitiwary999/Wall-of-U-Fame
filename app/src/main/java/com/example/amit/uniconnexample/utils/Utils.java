package com.example.amit.uniconnexample.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.amit.uniconnexample.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by amit on 30/10/16.
 */

public class Utils {
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input) {

        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static MaterialDialog loadingDialog(Context context) {
        return new MaterialDialog.Builder(context)
                .progress(true, 100)
                .content("Loading..")
                .build();
    }

    public static void setUpToolbarBackButton(final AppCompatActivity activity, Toolbar toolbar) {
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(activity, R.drawable.ic_arrow_back_white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        });
    }
    public static void setupToolBarBackButton(final Fragment fragment,Toolbar toolbar){
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
    }
}