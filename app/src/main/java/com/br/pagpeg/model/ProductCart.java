package com.br.pagpeg.model;

import java.io.Serializable;

/**
 * Created by brunolemgruber on 06/10/16.
 */

public class ProductCart implements Serializable {

   private Integer quantity;

   private Double price_total;

   private Product product;

   private String name;

   public ProductCart(){
   }

    public ProductCart(String name,Integer quantity,Double price_total){
        this.quantity = quantity;
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

    public Double getPrice_total() {
        return price_total;
    }

    public void setPrice_total(Double price_total) {
        this.price_total = price_total;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
