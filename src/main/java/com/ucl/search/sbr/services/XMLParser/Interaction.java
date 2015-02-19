package com.ucl.search.sbr.services.XMLParser;

import java.util.List;

/**
 * Created by root on 18/02/15.
 */
public class Interaction {

    private String query;
    private List<InteractionResult> results;
    private List<ClickedResult> clicked;
    private String num;
    private String starttime;
    private String type;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<InteractionResult> getResults() {
        return results;
    }

    public void setResults(List<InteractionResult> results) {
        this.results = results;
    }

    public List<ClickedResult> getClicked() {
        return clicked;
    }

    public void setClicked(List<ClickedResult> clicked) {
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
