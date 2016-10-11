package com.br.pagpeg.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by brunolemgruber on 06/10/16.
 */

public class Cart implements Serializable {

    private int count;

    private List<Product> products;

    private String total_price;

    private String discount;

    private String tax;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
