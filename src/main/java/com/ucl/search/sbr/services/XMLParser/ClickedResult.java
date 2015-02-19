package com.ucl.search.sbr.services.XMLParser;

/**
 * Created by root on 18/02/15.
 */
public class ClickedResult {

    private String num;
    private String starttime;
    private String endtime;
    private String rank;
    private String docno;

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

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getDocno() {
        return docno;
    }

    public void setDocno(String docno) {
        this.docno = docno;
    }

}
