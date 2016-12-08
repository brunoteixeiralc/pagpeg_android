package com.br.pagpeg.model;

import java.io.Serializable;

/**
 * Created by brunolemgruber on 06/10/16.
 */

public class ProductCart implements Serializable {

   private Integer quantity;

   private Integer shopper_quantity;

   private Double price_total;

   private Double shopper_price_unit;

   private Double shopper_price_total;

   private Double price_unit;

   private Product product;

   private String name;

   private String status;

   public ProductCart(){
   }

    public ProductCart(String name,Integer quantity,Double price_total,String status, Double price_unit){
        this.quantity = quantity;
        this.price_total = price_total;
        this.status = status;
        this.price_unit = price_unit;
    }

    public Double getPrice_unit() {
        return price_unit;
    }

    public void setPrice_unit(Double price_unit) {
        this.price_unit = price_unit;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getShopper_price_total() {
        return shopper_price_total;
    }

    public void setShopper_price_total(Double shopper_price_total) {
        this.shopper_price_total = shopper_price_total;
    }

    public Double getShopper_price_unit() {
        return shopper_price_unit;
    }

    public void setShopper_price_unit(Double shopper_price_unit) {
        this.shopper_price_unit = shopper_price_unit;
    }

    public Integer getShopper_quantity() {
        return shopper_quantity;
    }

    public void setShopper_quantity(Integer shopper_quantity) {
        this.shopper_quantity = shopper_quantity;
    }
}
