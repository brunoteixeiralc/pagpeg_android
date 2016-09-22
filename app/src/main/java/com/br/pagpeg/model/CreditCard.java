package com.br.pagpeg.model;

import java.io.Serializable;

/**
 * Created by brunolemgruber on 19/07/16.
 */

public class CreditCard implements Serializable {

    private String cc_name;

    private String cc_expire_date;

    private String cc_number;

    private String cc_security_number;

    private String cc_flag;

    private String token;

    private int cc_flag_img;

    private boolean isDefault;

    public CreditCard(){

    }

    public CreditCard(String cc_name,String cc_expire_date,String cc_number,String cc_security_number,String cc_flag,String token, int cc_flag_img){
        this.cc_name = cc_name;
        this.cc_expire_date = cc_expire_date;
        this.cc_number = cc_number;
        this.cc_security_number = cc_security_number;
        this.cc_flag = cc_flag;
        this.token = token;
        this.cc_flag_img = cc_flag_img;
    }

    public String getCc_name() {
        return cc_name;
    }

    public void setCc_name(String cc_name) {
        this.cc_name = cc_name;
    }

    public String getCc_expire_date() {
        return cc_expire_date;
    }

    public void setCc_expire_date(String cc_expire_date) {
        this.cc_expire_date = cc_expire_date;
    }

    public String getCc_number() {
        return cc_number;
    }

    public void setCc_number(String cc_number) {
        this.cc_number = cc_number;
    }

    public String getCc_security_number() {
        return cc_security_number;
    }

    public void setCc_security_number(String cc_security_number) {
        this.cc_security_number = cc_security_number;
    }

    public String getCc_flag() {
        return cc_flag;
    }

    public void setCc_flag(String cc_flag) {
        this.cc_flag = cc_flag;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCc_flag_img() {
        return cc_flag_img;
    }

    public void setCc_flag_img(int cc_flag_img) {
        this.cc_flag_img = cc_flag_img;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
