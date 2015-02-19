package com.ucl.search.sbr.services.XMLParser;

/**
 * Created by root on 18/02/15.
 */
public class Interaction {

    private String query;
    private InteractionResult[] results;
    private ClickedResult[] clicked;
    private String num;
    private String starttime;
    private String type;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public InteractionResult[] getResults() {
        return results;
    }

    public void setResults(InteractionResult[] results) {
        this.results = results;
    }

    public ClickedResult[] getClicked() {
        return clicked;
    }

    public void setClicked(ClickedResult[] clicked) {
        this.clicked = clicked;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
