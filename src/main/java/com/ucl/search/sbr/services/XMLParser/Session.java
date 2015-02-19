package com.ucl.search.sbr.services.XMLParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 18/02/15.
 */
public class Session {

    private Topic topic;
    private List<Interaction> interactions = new ArrayList<Interaction>();
    private CurrentQuery curQuery;
    private String num;
    private String starttime;
    private String userid;

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public List<Interaction> getInteractions() {
        return interactions;
    }

    public void setInteractions(List<Interaction> interactions) {
        this.interactions = interactions;
    }

    public CurrentQuery getCurQuery() {
        return curQuery;
    }

    public void setCurQuery(CurrentQuery curQuery) {
        this.curQuery = curQuery;
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
