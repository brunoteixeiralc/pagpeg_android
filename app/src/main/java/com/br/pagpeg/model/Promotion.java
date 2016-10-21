package com.br.pagpeg.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by brunolemgruber on 19/10/16.
 */

public class Promotion implements Serializable {

    private String key;

    private String name;

    private Long start;

    private Long end;

    private List<PromotionProduct> promotionProducts;

    public List<PromotionProduct> getPromotionProducts() {
        return promotionProducts;
    }

    public void setPromotionProducts(List<PromotionProduct> promotionProducts) {
        this.promotionProducts = promotionProducts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
