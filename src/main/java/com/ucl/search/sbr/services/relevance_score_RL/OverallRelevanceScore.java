package com.ucl.search.sbr.services.relevance_score_RL;

import com.ucl.search.sbr.services.entityDb.MysqlEntityMetricsProvider;
import com.ucl.search.sbr.services.entityExtraction.Entity;
import com.ucl.search.sbr.services.entityExtraction.Interaction;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ralucamelon on 15/03/2015.
 */
public class OverallRelevanceScore {

    /**
     * define the parameters for the overall score calculation
     * these params are set to match the different weight adjustment policies
     */
    private final float A = (float) 2.2;
    private final float B = (float) 1.8;
    private final float C = (float) 0.07;
    private final float D = (float) 0.4;

    private CurrentRelevance currentRelevance;

    public OverallRelevanceScore(MysqlEntityMetricsProvider mysqlEntityMetricsProvider) {
        this.currentRelevance = new CurrentRelevance(mysqlEntityMetricsProvider);
    }

    /**
     * Calculates the new score between query and document.
     * Takes into account the different policies defined based on each session dynamics.
     *
     * @param query    query under evaluation
     * @param docId    the document id for which the score needs to be calculated
     * @param themeE   a list with the theme entities identified for the current query
     * @param addedE   a list with the added entities identified for the current query
     * @param removedE a list with the removed entities identified for the current query
     * @param weights  HashMap containing <entityId, entityWeight>; the updated weights through RL for each entity
     * @return the newly calculated score between query and document Score(q,d)
     */
    public double getNewScore(Interaction query, String docId, List<Entity> themeE, List<Entity> addedE, List<Entity> removedE, HashMap<String, Double> weights) {

        double newScore;

        /* get the current score between query and document */
        double currentScore = currentRelevance.calculateCurrentRelevance(query, docId, false);
        //System.out.println("here the current score is: " + currentScore);
        //currentScore = Math.log(currentScore);

//        System.out.println("here the weights passed  ");
//        for(String key: weights.keySet()){
//            System.out.println(key + "   weight:  " + weights.get(key));
//        }

        /** treat the theme entities case and apply the formula (sum all new weights for all theme entities) */
        double themeEntityCase = 0.0;

        for (Entity e : themeE) {
            themeEntityCase += weights.get(e.getMid());
        }


        /* treat the added entities cases:
         * case1: added entity belongs to RDi-1 (previously retrieved relevant docs)
         * case2: added entity DOESN'T belong to RDi-1 (previously retrieved relevant docs)
         * and apply the formula (sum all new weights for each case)  */
        double addedEntityCase1 = 0.0;
        double addedEntityCase2 = 0.0;

        for (Entity e : addedE) {
            addedEntityCase1 += weights.get(e.getMid());
        }

        /** treat the removed entities case and apply the formula (sum all new weights for all removed entities) */
        double removedEntityCase = 0.0;

        for (Entity e : removedE) {
            removedEntityCase += weights.get(e.getMid());
        }


        //System.out.println(themeEntityCase + "    " + addedEntityCase1 + "   " + addedEntityCase2 + "   " + removedEntityCase);
        /**Overall relevance score between query and document after RL weight adjustment */
        newScore = currentScore + A * themeEntityCase - B * addedEntityCase1 + C * addedEntityCase2 - D * removedEntityCase;

        //System.out.println("new score: before return " + newScore + "  formula based on:current score + - + - " + currentScore);

        return newScore;
    }

    /**
     * This method is called for the first query in the session
     * In this case there are no theme, added or removed entities
     *
     * @param query query under evaluation
     * @param docId the document id for which the score needs to be calculated
     * @return the newly calculated score between query and document Score(q,d)
     */
    public double getNewScore(Interaction query, String docId) {

        double currentScore = currentRelevance.calculateCurrentRelevance(query, docId, false);

        if(currentScore == 0.0){
            return 1.0;
        }

        //currentScore = Math.log(currentScore);

        return currentScore;
    }

}
