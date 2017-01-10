package com.br.pagpeg.utils;

import android.content.Context;

import com.br.pagpeg.R;

/**
 * Created by brunolemgruber on 09/01/17.
 */

public final class ErrorException {

    public static String authFirebaseError(String exception,Context context){

        if (exception.contains("The email address is badly formatted.")) {
            return context.getString(R.string.error_wrong_email);
        }
        else
        if (exception.contains("There is no user record corresponding to this identifier. The user may have been deleted.")) {
            return context.getString(R.string.error_user_not_exist);
        }
        else
        if (exception.contains("The password is invalid or the user does not have a password")) {
            return context.getString(R.string.error_wrong_password);
        }

        return "";
    }
}
