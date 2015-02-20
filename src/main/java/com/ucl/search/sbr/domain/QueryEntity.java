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

    // define the setters and getters
    public String getQueryText() {
        return this.queryText;
    }

    public void setQueryText(String qText) {
        this.queryText = qText;
    }

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

}
