package com.ucl.search.sbr.services.maximum_reward;

import com.ucl.search.sbr.services.entityDb.MysqlEntityMetricsProvider;
import com.ucl.search.sbr.services.entityExtraction.Interaction;
import com.ucl.search.sbr.services.query_submission.QuerySubmitter;
import com.ucl.search.sbr.services.relevance_score_RL.CurrentRelevance;
import lemurproject.indri.ParsedDocument;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ralucamelon on 14/03/2015.
 */
public class MaxRewardDocExtractor {

    private final boolean MLE_BASED_SCORE = true;


    private CurrentRelevance relevanceScore;

    public MaxRewardDocExtractor(MysqlEntityMetricsProvider entityMetricsProvider) {
        this.relevanceScore = new CurrentRelevance(entityMetricsProvider);
    }

    /**
     * 1. Submits the query to indri and retrieves the first 10 documents
     * 2. For each document retrieved it calculated the score between doc and the query
     * 3. Builds the HashMap between docId and the score calculated at step 2
     * HashMap will look something like:
     * (docId0, score(query,doc0)
     * (docId1, score(query,doc1)
     * ...
     * (docIdn, score(query,docN)
     *
     * @param query the query that will be submitted to indri
     */
    public HashMap<String, Double> buildQueryDocScore(Interaction query) {

        HashMap<String, Double> queryDocScore = new HashMap<>();
        double score;

        QuerySubmitter querySubmitter = new QuerySubmitter();
        ParsedDocument[] results = querySubmitter.getResultsForQuery(query.getQuery(), 10);

        /* for each document in results compute the score(query,doc) and store it in the HashMap */
        for(ParsedDocument doc : results){
            String docId = new String((byte[])doc.metadata.get("docno"));
            score = relevanceScore.calculateCurrentRelevance(query, docId, MLE_BASED_SCORE);
            // System.out.println("in maximing hashmap : score is: " + score);
            queryDocScore.put(docId, score);
        }

        return queryDocScore;
    }


    /** returns the id of the document with the maximum value in the HashTable (maximum reward) */
    public String getMaxRewardingDoc(HashMap<String, Double> scores) {

        String maxRewardingDocID = new String();
        Double maxRelevanceScore = Double.MIN_VALUE;

        for(String key : scores.keySet()){
            if(scores.get(key) > maxRelevanceScore){
                maxRelevanceScore = scores.get(key);
                maxRewardingDocID = key;
            }
        }

        return maxRewardingDocID;
    }

    public ArrayList<String> getRDids(HashMap<String, Double> queryDocScore){

        ArrayList<String> RDids = new ArrayList<>(queryDocScore.keySet());

//        System.out.println("the array of ids for RDi is :");
//        for(String s: RDids){
//            System.out.println(s);
//        }

        return RDids;
    }

}
