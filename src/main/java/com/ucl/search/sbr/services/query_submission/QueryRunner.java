package com.ucl.search.sbr.services.query_submission;

import com.google.common.base.CharMatcher;
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

        sanitiseQueryRequest(request);
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
            return parsedDocs;

        } catch (Exception e) {
            e.printStackTrace();
        }

        env.close();
        return null;
    }

    private void sanitiseQueryRequest(QueryRequest request) {
        request.query = CharMatcher.anyOf(":;@+-_=<>|~?!()/\\,.\"\'").removeFrom(request.query);
    }


}
