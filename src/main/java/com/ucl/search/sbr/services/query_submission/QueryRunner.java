package com.ucl.search.sbr.services.query_submission;

import lemurproject.indri.*;

/**
 * Created by ralucamelon on 19/02/2015.
 */
public class QueryRunner {

    public QueryRunner(){}

    public ParsedDocument[] submitQuery(QueryRequest request) throws Exception {

        QueryEnvironment env = new QueryEnvironment();

        try {
            env.addIndex("/usr/local/index/");
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* run the query and get the results */
        QueryResults results = env.runQuery(request);
        QueryResult[] queryRes = results.results;

        int[] docIds = new int[queryRes.length];

        /* iterate over the results and build the array with document ids */
        for(int index = 0; index < queryRes.length; index++) {
            docIds[index] = queryRes[index].docid;
        }

        /* get the parsed documents based on their ids */
        try {
            ParsedDocument[] parsedDocs = env.documents(docIds);
            System.out.println(parsedDocs.length);
            return parsedDocs;

        } catch (Exception e) {
            e.printStackTrace();
        }

        env.close();
        return null;
    }
}
