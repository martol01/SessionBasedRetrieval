package com.ucl.search.sbr.services.XMLParser;

/**
 * Created by root on 18/02/15.
 */
public class InteractionResult {

    private String rank;
    private String url;
    private String clueweb12id;
    private String title;
    private String snippet;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClueweb12id() {
        return clueweb12id;
    }

    public void setClueweb12id(String clueweb12id) {
        this.clueweb12id = clueweb12id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

}
