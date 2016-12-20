package com.br.pagpeg.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by brunolemgruber on 06/10/16.
 */

public class Cart implements Serializable {

    private int count;

    private List<ProductCart> productCartList;

    //for firebase
    private HashMap<String,ProductCart> products;

    private Double total_price;

    private Double total_price_shopper;

    private Double discount;

    private Double tax;

    private String network;

    private String store;

    private String shopper;

    private String status;

    private Double total;

    private Double total_shopper;

    private String user;

    private Boolean rated;

    private Integer rate;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getShopper() {
        return shopper;
    }

    public void setShopper(String shopper) {
        this.shopper = shopper;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ProductCart> getProductCartList() {
        return productCartList;
    }

    public void setProductCartList(List<ProductCart> productCartList) {
        this.productCartList = productCartList;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(Double total_price) {
        this.total_price = total_price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public HashMap<String, ProductCart> getProducts() {
        return products;
    }

    public void setProducts(HashMap<String, ProductCart> products) {
        this.products = products;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Double getTotal_shopper() {
        return total_shopper;
    }

    public void setTotal_shopper(Double total_shopper) {
        this.total_shopper = total_shopper;
    }

    public Double getTotal_price_shopper() {
        return total_price_shopper;
    }

    public void setTotal_price_shopper(Double total_price_shopper) {
        this.total_price_shopper = total_price_shopper;
    }

    public Boolean getRated() {
        return rated;
    }

    public void setRated(Boolean rated) {
        this.rated = rated;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }
}
