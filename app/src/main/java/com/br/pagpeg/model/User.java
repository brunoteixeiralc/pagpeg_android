package com.br.pagpeg.model;

import java.io.Serializable;

/**
 * Created by brunolemgruber on 14/09/16.
 */

public class User implements Serializable {

    private String name;

    private String number;

    private String email;

    private String user_img;

    private String device_id;

    public User(){

    }

    public User(String name,String number,String email,String user_img,String device_id){
        this.name = name;
        this.number = number;
        this.email = email;
        this.user_img = user_img;
        this.device_id = device_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
}
