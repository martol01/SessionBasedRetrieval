package com.ucl.search.sbr.services.dexterEntityLinking;

/**
 * Created by root on 18/02/15.
 */
public class Spot {


    private String mention;
    private double linkProbability;
    private int start;
    private int end;
    private int linkFrequency;
    private int docFrequency;
    private long entity;
    private String field;
    private String wikiname;
    private int entityFrequency;
    private double commonness;
    private double score;

    public String getMention() {
        return mention;
    }

    public void setMention(String mention) {
        this.mention = mention;
    }

    public double getLinkProbability() {
        return linkProbability;
    }

    public void setLinkProbability(double linkProbability) {
        this.linkProbability = linkProbability;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getLinkFrequency() {
        return linkFrequency;
    }

    public void setLinkFrequency(int linkFrequency) {
        this.linkFrequency = linkFrequency;
    }

    public int getDocFrequency() {
        return docFrequency;
    }

    public void setDocFrequency(int docFrequency) {
        this.docFrequency = docFrequency;
    }

    public long getEntity() {
        return entity;
    }

    public void setEntity(long entity) {
        this.entity = entity;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getWikiname() {
        return wikiname;
    }

    public void setWikiname(String wikiname) {
        this.wikiname = wikiname;
    }

    public int getEntityFrequency() {
        return entityFrequency;
    }

    public void setEntityFrequency(int entityFrequency) {
        this.entityFrequency = entityFrequency;
    }

    public double getCommonness() {
        return commonness;
    }

    public void setCommonness(double commonness) {
        this.commonness = commonness;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
