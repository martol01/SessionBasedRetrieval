package com.ucl.search.sbr.services.entityExtraction;

/**
 * Created by root on 04/03/15.
 */
public class Entity {
    private String entity;
    private String mid;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMention() {
        return entity;
    }

    public void setMention(String entity) {
        this.entity = entity;
    }
}
