package com.ucl.search.sbr.services.entityExtraction;

/**
 * Created by root on 04/03/15.
 */
public class Session {
    private String id;
    private Interaction[] interactions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Interaction[] getInteractions() {
        return interactions;
    }

    public void setInteractions(Interaction[] interactions) {
        this.interactions = interactions;
    }
}
