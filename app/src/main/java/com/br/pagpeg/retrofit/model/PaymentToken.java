package com.br.pagpeg.retrofit.model;

/**
 * Created by brunolemgruber on 14/12/16.
 */

public class PaymentToken {

    private String id;

    private ExtraInfo extra_info;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ExtraInfo getExtra_info() {
        return extra_info;
    }

    public void setExtra_info(ExtraInfo extra_info) {
        this.extra_info = extra_info;
    }
}
