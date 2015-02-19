package com.ucl.search.sbr.services.dexterEntityLinking;

/**
 * Created by root on 18/02/15.
 */
public class AnnotateResponse {
    private Document doc;
    private AnnotatedDocument annotatedDoc;
    private Spot[] spots;

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public AnnotatedDocument getAnnotatedDoc() {
        return annotatedDoc;
    }

    public void setAnnotatedDoc(AnnotatedDocument annotatedDoc) {
        this.annotatedDoc = annotatedDoc;
    }

    public Spot[] getSpots() {
        return spots;
    }

    public void setSpots(Spot[] spots) {
        this.spots = spots;
    }
}
