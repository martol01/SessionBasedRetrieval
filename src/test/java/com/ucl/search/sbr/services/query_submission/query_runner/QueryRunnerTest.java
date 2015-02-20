package com.ucl.search.sbr.services.query_submission.query_runner;

import com.ucl.search.sbr.services.query_submission.query_builder.QueryBuilder;
import com.ucl.search.sbr.services.query_submission.result.ResultHandler;
import junit.framework.TestCase;
import lemurproject.indri.QueryRequest;
import lemurproject.indri.QueryResults;
import org.junit.Test;

public class QueryRunnerTest extends TestCase {

//    private QueryRunner queryRunner;
//    private QueryBuilder builder;
//    private QueryRequest request;
//
//
//    @Before
//    public void createObjInstance() {
//
//        queryRunner = new QueryRunner();
//        builder = new QueryBuilder();
//        request = new QueryRequest();
//    }


    /* test the properties of query request and check the results (format, total number of docs retrieved etc) */
    @Test
    public void testQueryRunner() throws Exception {

        /* create necessary class instances */
        QueryRunner queryRunner = new QueryRunner();
        QueryBuilder builder = new QueryBuilder();
        QueryRequest request = new QueryRequest();

        /* initialize the expected values */
        String expectedQueryString = "food from China";
        int nbOfResults = 10;

        request = builder.buildQueryRequest(expectedQueryString, nbOfResults);


        assertEquals(expectedQueryString, request.query);
        assertEquals(nbOfResults, request.resultsRequested);


        QueryResults results = queryRunner.submitQuery(request);

        ResultHandler handler = new ResultHandler();
        handler.handleResult(results);

        assertTrue(results.results.length == nbOfResults);
    }
}