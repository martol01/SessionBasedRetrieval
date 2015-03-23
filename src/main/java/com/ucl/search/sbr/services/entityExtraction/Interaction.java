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

    public String getEntityQuery() {
        if (entities.length < 2)
            return query;

        StringBuilder queryBuilder = new StringBuilder();
        for (int i=0; i<entities.length - 1; i++) {
            queryBuilder.append(entities[i].getMention()).append(" ");
        }
        queryBuilder.append(entities[entities.length - 1]);
        return queryBuilder.toString();
    }

    public Entity[] getEntities() {
        return entities;
    }

    public void setEntities(Entity[] entities) {
        this.entities = entities;
    }
}
