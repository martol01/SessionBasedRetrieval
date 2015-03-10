package com.ucl.search.sbr.services.entities_frequency;

/**
 * Created by root on 09/03/15.
 */
public class CluewebDocument {
    private String docid;
    private CluewebEntity[] entities;

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public CluewebEntity[] getEntities() {
        return entities;
    }

    public void setEntities(CluewebEntity[] entities) {
        this.entities = entities;
    }
}
