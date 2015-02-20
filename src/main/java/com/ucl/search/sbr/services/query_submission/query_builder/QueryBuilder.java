package com.ucl.search.sbr.services.query_submission.query_builder;

import lemurproject.indri.QueryRequest;

/**
 * Created by ralucamelon on 19/02/2015.
 */
public class QueryBuilder {

    public QueryBuilder() {}

    public QueryRequest buildQueryRequest(String query, int resultsNb){

        QueryRequest req = new QueryRequest();
        req.query = query;
        req.resultsRequested = resultsNb;

        //req.options = req.TextSnippet; //alternative one is TextSnippet
        return req;
    }

}
