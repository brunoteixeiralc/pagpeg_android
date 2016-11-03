package com.br.pagpeg;

import android.app.Application;

import com.onesignal.OneSignal;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by brunolemgruber on 14/07/16.
 */

public class PagPegApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this).init();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Nexa-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
