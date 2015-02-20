package com.ucl.search.sbr.services.query_submission.query_runner;

import lemurproject.indri.QueryEnvironment;
import lemurproject.indri.QueryRequest;
import lemurproject.indri.QueryResults;

/**
 * Created by ralucamelon on 19/02/2015.
 */
public class QueryRunner {

    public QueryRunner(){}

    public QueryResults submitQuery(QueryRequest request) throws Exception {

        QueryEnvironment env = new QueryEnvironment();

        try {
            env.addIndex("/Users/ralucamelon/Documents/UCL3/ResearchMethods/indri-5.8/outputIndex/");
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* run the query and get the results */
        QueryResults results = env.runQuery(request);
        env.close();

        return results;
    }
}
