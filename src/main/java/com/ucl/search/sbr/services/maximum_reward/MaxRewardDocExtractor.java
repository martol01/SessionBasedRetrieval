package com.ucl.search.sbr.services.maximum_reward;

import com.ucl.search.sbr.services.entityExtraction.Interaction;
import com.ucl.search.sbr.services.query_submission.QuerySubmitter;
import com.ucl.search.sbr.services.relevance_score_RL.CurrentRelevance;
import lemurproject.indri.ParsedDocument;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ralucamelon on 14/03/2015.
 */
public class MaxRewardDocExtractor {

    private final boolean MLE_BASED_SCORE = true;

    public MaxRewardDocExtractor() {}

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
        CurrentRelevance relevanceScore = new CurrentRelevance();

        double score;

        QuerySubmitter querySubmitter = new QuerySubmitter();
        ParsedDocument[] results = querySubmitter.getResultsForQuery(query.getQuery(), 100);

        /* for each document in results compute the score(query,doc) and store it in the HashMap */
        for(ParsedDocument doc : results){
            String docId = new String((byte[])doc.metadata.get("docno"));

            System.out.println(new String((byte[])doc.metadata.get("docno")));

            score = relevanceScore.calculateCurrentRelevance(query, docId, MLE_BASED_SCORE);
            queryDocScore.put(docId, score);
        }

        return queryDocScore;
    }


    /** returns the id of the document with the maximim value in the HashTable (maximim reward) */
    public String getMaxRewardingDoc(HashMap<String, Double> scores) {

        String maxRewardingDocID = null;
        Double maxRelevanceScore = Double.MIN_VALUE;

        for(Map.Entry<String, Double> score : scores.entrySet()){
            if(score.getValue() > maxRelevanceScore){
                maxRelevanceScore = score.getValue();
                maxRewardingDocID = score.getKey();
            }
        }

        return maxRewardingDocID;
    }

}
