package com.br.pagpeg;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.br.pagpeg.activity.shopper.MainShopperActivity;
import com.br.pagpeg.activity.user.MainUserActivity;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OSNotificationReceivedResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by brunolemgruber on 14/07/16.
 */

public class PagPegApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationOpenedHandler(new NotificationOpenedHandler())
                .init();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Nexa-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    private class NotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {

            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;

            if (actionType == OSNotificationAction.ActionType.ActionTaken){

                Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);

                if(result.action.actionID.equalsIgnoreCase("visualizar_id")){

                    String uid = (String) data.opt("shopper_uid");

                    Intent intent = new Intent(PagPegApplication.this.getApplicationContext(), MainShopperActivity.class);
                    intent.putExtra("shopper_uid",uid);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }else if (result.action.actionID.equalsIgnoreCase("visualizar_ordem_shopper")){

                    String uid = (String) data.opt("user_uid");
                    String status = (String) data.opt("status");

                    Intent intent = new Intent(PagPegApplication.this.getApplicationContext(), MainUserActivity.class);
                    intent.putExtra("user_uid",uid);
                    intent.putExtra("status",status);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            }
        }
    }

    private class NotificationExtender extends NotificationExtenderService {
        @Override
        protected boolean onNotificationProcessing(OSNotificationReceivedResult notification) {
            return false;
        }
    }

    private class NotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
        @Override
        public void notificationReceived(OSNotification notification) {

        }
    }
}

