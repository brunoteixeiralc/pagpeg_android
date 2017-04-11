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

    private String one_signal_key;

    private String id_iugu;

    public User(){

    }

    public User(String name,String number,String email,String user_img,String device_id,String one_signal_key, String id_iugu){
        this.name = name;
        this.number = number;
        this.email = email;
        this.user_img = user_img;
        this.device_id = device_id;
        this.one_signal_key = one_signal_key;
        this.id_iugu = id_iugu;
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

    public String getOne_signal_key() {
        return one_signal_key;
    }

    public void setOne_signal_key(String one_signal_key) {
        this.one_signal_key = one_signal_key;
    }

    public String getId_iugu() {
        return id_iugu;
    }

    public void setId_iugu(String id_iugu) {
        this.id_iugu = id_iugu;
    }

}
