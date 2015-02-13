package com.ucl.search.sbr.domain;

/**
 * Created by Gabriel on 2/11/2015.
 */
public class QueryEntity {

    private static final double DEFAULT_WEIGHT = 1;

    private double weight = DEFAULT_WEIGHT;
    private String queryText;

    public QueryEntity(double weight, String queryText) {
        this.weight = weight;
        this.queryText = queryText;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }
}
