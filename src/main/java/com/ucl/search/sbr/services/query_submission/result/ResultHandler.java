package com.ucl.search.sbr.services.query_submission.result;

import lemurproject.indri.QueryResult;
import lemurproject.indri.QueryResults;

/**
 * Created by Gabriel on 2/11/2015.
 */
public class ResultHandler {

    public ResultHandler(){}

    public void handleResult(QueryResults results) {

        /* print the document name, score and snippet */

        QueryResult[] queryRes = results.results;

             /* iterate over the results and print them */
        for (QueryResult result : queryRes) {
            System.out.println("document name: " + result.documentName);
            System.out.println();
            System.out.println(" score of doc " + result.score);
            System.out.println();
            System.out.println(" snippet: " + result.snippet);
            System.out.println();
        }

    }

}
