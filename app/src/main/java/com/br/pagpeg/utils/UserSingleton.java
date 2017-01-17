package com.br.pagpeg.utils;

import com.br.pagpeg.model.User;

/**
 * Created by brunolemgruber on 17/01/17.
 */

public class UserSingleton {

    private User user;

    private static UserSingleton instance ;

    private UserSingleton() {
       user = null;
    }

    public static UserSingleton getInstance () {
        if ( UserSingleton.instance == null ) {
            UserSingleton.instance = new UserSingleton();
        }
        return UserSingleton.instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
