package com.br.pagpeg.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.pagpeg.R;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

/**
 * Created by brunolemgruber on 14/09/16.
 */

public final class Utils {

    private static SimpleArcDialog simpleArcDialog;
    private static ArcConfiguration arcConfiguration;
    private static ImageView mIconList,mIconMap,mIconBarcode,mIconCreditCard;
    private static TextView mIconLogout;

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void openKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_FORCED);
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
        if(simpleArcDialog != null)
            simpleArcDialog.dismiss();
    }

    public static void setIconBar(EnumIconBar enumIconBar, Toolbar toolbar){

        mIconList = (ImageView) toolbar.findViewById(R.id.ic_listStore);
        mIconMap = (ImageView) toolbar.findViewById(R.id.ic_mapStore);
        mIconLogout = (TextView) toolbar.findViewById(R.id.logout);
        mIconBarcode = (ImageView) toolbar.findViewById(R.id.ic_bar_code);
        mIconCreditCard = (ImageView) toolbar.findViewById(R.id.ic_add_credit_card);

        mIconList.setVisibility(View.GONE);
        mIconMap.setVisibility(View.GONE);
        mIconLogout.setVisibility(View.GONE);
        mIconBarcode.setVisibility(View.GONE);
        mIconCreditCard.setVisibility(View.GONE);

        switch (enumIconBar){
            case STORELIST:{
                mIconMap.setVisibility(View.VISIBLE);
                break;
            }
            case CART:{
                break;
            }
            case STOREMAP:{
                mIconList.setVisibility(View.VISIBLE);
                break;
            }
            case PROFILE:{
                mIconLogout.setVisibility(View.VISIBLE);
                break;
            }
            case PRODUCTSDETAIL:{
                break;
            }
            case STOREPRODUCTS:{
                mIconBarcode.setVisibility(View.VISIBLE);
                break;
            }
            case ADDCREDITCARD:{
                mIconCreditCard.setVisibility(View.VISIBLE);
                break;
            }
            case EDITPROFILE:{
                break;
            }
            case STOREDETAIL:{
                break;
            }
            case FINDINGSHOPPER:{
                break;
            }

            default:break;
        }

    }
}
