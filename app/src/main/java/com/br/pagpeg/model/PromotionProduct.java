package com.br.pagpeg.model;

import java.io.Serializable;

/**
 * Created by brunolemgruber on 19/10/16.
 */

public class PromotionProduct implements Serializable {

    private String name_product;

    private Long start;

    private Long end;

    private int quantity;

    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
