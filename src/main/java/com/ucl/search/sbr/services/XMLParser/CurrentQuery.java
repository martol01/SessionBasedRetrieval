package com.ucl.search.sbr.services.XMLParser;

/**
 * Created by root on 18/02/15.
 */
public class CurrentQuery {
    private String query;
    private String starttime;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }
    public String toString(){
        return this.starttime + "; "+this.query;
    }
}
