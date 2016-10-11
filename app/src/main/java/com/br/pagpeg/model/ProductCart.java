package com.br.pagpeg.model;

import java.io.Serializable;

/**
 * Created by brunolemgruber on 06/10/16.
 */

public class ProductCart implements Serializable {

   private int quantity;

   private String price_total;

    public ProductCart(){
    }

    public ProductCart(String name,int quantity,String price_total){
        this.quantity = quantity;
        this.price_total = price_total;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPrice_total() {
        return price_total;
    }

    public void setPrice_total(String price_total) {
        this.price_total = price_total;
    }
}
