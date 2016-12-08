package com.br.pagpeg.utils;

/**
 * Created by brunolemgruber on 07/10/16.
 */

public class EnumStatus {

    public enum Status {

        FINDING_SHOPPER("Finding Shopper"),
        SHOPPER_BUYING("Shopper Buying"),
        WAITING_USER_APPROVE("Waiting User To Approve"),
        USER_APPROVE("User Approve"),
        SHOPPER_PAYING("Shopper paying"),
        SHOPPER_PAYED("Shopper payed"),
        USER_RECEIVED("User Received"),

        PRODUCT_NOT_FIND("Not Find"),
        PRODUCT_WAITING("Waiting Status"),
        PRODUCT_FIND("Find");

        private String name;

        Status(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

}

