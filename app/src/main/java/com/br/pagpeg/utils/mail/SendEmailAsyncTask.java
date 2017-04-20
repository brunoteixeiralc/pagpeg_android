package com.br.pagpeg.utils.mail;

import android.os.AsyncTask;
import android.util.Log;

import com.br.pagpeg.activity.user.ProductRegisterActivity;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

/**
 * Created by brunolemgruber on 19/04/17.
 */

public class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {

    public Mail m;
    public ProductRegisterActivity activity;

    public SendEmailAsyncTask() {}

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            m.send();
            return true;
        } catch (AuthenticationFailedException e) {
            Log.e(SendEmailAsyncTask.class.getName(), "Bad account details");
            e.printStackTrace();
            return false;
        } catch (MessagingException e) {
            Log.e(SendEmailAsyncTask.class.getName(), "Email failed");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
