package com.br.pagpeg.model;

import java.io.Serializable;

/**
 * Created by brunolemgruber on 06/10/16.
 */

public class ProductCart implements Serializable {

   private int quantity;

   private String price_total;

   private Product product;

   private String name;

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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
