package com.ucl.search.sbr.services.entityExtraction;

/**
 * Created by root on 04/03/15.
 */
public class Interaction {
    private String num;
    private String query;
    private Entity[] entities;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Entity[] getEntities() {
        return entities;
    }

    public void setEntities(Entity[] entities) {
        this.entities = entities;
    }
}
