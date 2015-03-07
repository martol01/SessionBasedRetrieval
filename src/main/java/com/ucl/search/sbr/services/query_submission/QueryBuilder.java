package com.ucl.search.sbr.services.query_submission;

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

        //req.options = one of QueryRequest.HTMLSnippet or QueryRequest.TextSnippet. HTML snippet uses <strong> tags to highlight matched terms in the snippet. Text snippet uses UPPERCASE to highlight matched terms.
        //req.options = req.TextSnippet;

        return req;
    }

}
