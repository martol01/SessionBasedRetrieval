package com.ucl.search.sbr.services.XMLParser;

/**
 * Created by root on 18/02/15.
 */
public class Topic {
    private String desc;
    private String num;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String toString(){
        return this.num + "; "+this.desc;
    }
}
