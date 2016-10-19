package com.br.pagpeg.model;

import java.io.Serializable;

/**
 * Created by brunolemgruber on 18/07/16.
 */

public class Product implements Serializable {

    private String name;

    private Long bar_code;

    private String category;

    private String description;

    private String img;

    private Double price;

    private String short_unit_measurement;

    private String unit_measurement;

    private String unit_quantity;

    private Double wholesale_price;

    private int quatity;

    private boolean inCart;

    public Long getBar_code() {
        return bar_code;
    }

    public void setBar_code(Long bar_code) {
        this.bar_code = bar_code;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getShort_unit_measurement() {
        return short_unit_measurement;
    }

    public void setShort_unit_measurement(String short_unit_measurement) {
        this.short_unit_measurement = short_unit_measurement;
    }

    public String getUnit_measurement() {
        return unit_measurement;
    }

    public void setUnit_measurement(String unit_measurement) {
        this.unit_measurement = unit_measurement;
    }

    public String getUnit_quantity() {
        return unit_quantity;
    }

    public void setUnit_quantity(String unit_quantity) {
        this.unit_quantity = unit_quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuatity() {
        return quatity;
    }

    public void setQuatity(int quatity) {
        this.quatity = quatity;
    }

    public boolean isInCart() {
        return inCart;
    }

    public void setInCart(boolean inCart) {
        this.inCart = inCart;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getWholesale_price() {
        return wholesale_price;
    }

    public void setWholesale_price(Double wholesale_price) {
        this.wholesale_price = wholesale_price;
    }
}
