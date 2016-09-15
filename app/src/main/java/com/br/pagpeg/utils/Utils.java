package com.br.pagpeg.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

/**
 * Created by brunolemgruber on 14/09/16.
 */

public final class Utils {

    private static SimpleArcDialog simpleArcDialog;
    private static ArcConfiguration arcConfiguration;

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void openDialog(Context context, String text){

        int[] colors = {Color.parseColor("#3fa9f5")};

        arcConfiguration = new ArcConfiguration(context);
        arcConfiguration.setLoaderStyle(SimpleArcLoader.STYLE.SIMPLE_ARC);
        arcConfiguration.setColors(colors);
        arcConfiguration.setText(text);
        simpleArcDialog = new SimpleArcDialog(context);
        simpleArcDialog.setConfiguration(arcConfiguration);
        simpleArcDialog.show();
    }

    public static void closeDialog(Context context){
        simpleArcDialog.dismiss();
    }
}
