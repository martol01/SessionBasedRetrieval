package com.ucl.search.sbr.services.query_submission;

import lemurproject.indri.ParsedDocument;

/**
 * Created by ralucamelon on 14/03/2015.
 */
public class QuerySubmitter {

    public QuerySubmitter(){}

    /** issues the query to indri and returns the results (baseline retrieval)
     *
     * @param query the query string
     * @param nbOfResults how many results indri should retrieve (e.g: 10, 1000)
     *
     * */

     public ParsedDocument[] getResultsForQuery(String query, int nbOfResults){

        QueryRunner queryRunner = new QueryRunner();
        QueryBuilder builder = new QueryBuilder();
        ParsedDocument[] results = new ParsedDocument[nbOfResults];

         try {
             results = queryRunner.submitQuery(builder.buildQueryRequest(query, nbOfResults));
             return results;
         } catch (Exception e) {
             e.printStackTrace();
         }

         return results;
     }
}
