package com.ucl.search.sbr.services.query_submission;

import lemurproject.indri.ParsedDocument;

/**
 * Created by Gabriel on 2/11/2015.
 */
public class ResultHandler {

    public ResultHandler() {
    }

    public void handleResult(ParsedDocument[] results) {

        /* iterate over the results and print the document content */
        for (ParsedDocument doc : results) {
            System.out.println(doc.content);
            //System.out.println(doc.text);
        }
    }

}
