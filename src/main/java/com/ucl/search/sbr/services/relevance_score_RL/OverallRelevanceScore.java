package com.ucl.search.sbr.services.relevance_score_RL;

import com.ucl.search.sbr.services.entityDb.MysqlEntityMetricsProvider;
import com.ucl.search.sbr.services.entityExtraction.Entity;
import com.ucl.search.sbr.services.entityExtraction.Interaction;
import com.ucl.search.sbr.services.transition_model_builder.WeightAdjustment;

import java.sql.SQLException;
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

    private final String host = "localhost";
    private final String username = "root";
    private final String password = "";

    private CurrentRelevance currentRelevance;
    private WeightAdjustment weightAdjuster;
    private MysqlEntityMetricsProvider mysqlMetricsProvider;

    public OverallRelevanceScore() {
        this.currentRelevance = new CurrentRelevance();
        this.weightAdjuster = new WeightAdjustment();
        try {
            this.mysqlMetricsProvider = new MysqlEntityMetricsProvider(host, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
     * @param maxDocId the id of the document with max relevance value from the previously retrieved docs
     * @param RDids    a list with document ids for the top 10 relevant documents that were previously retrieved
     *
     * @return the newly calculated score between query and document Score(q,d)
     */
    public double getNewScore(Interaction query, String docId, List<Entity> themeE, List<Entity> addedE, List<Entity> removedE, String maxDocId, List<String> RDids) {

        double newScore = 0.0;

        /* get the current score between query and document */
        double currentScore = currentRelevance.calculateCurrentRelevance(query, docId, false);
        currentScore = Math.log10(currentScore);

        /** treat the theme entities case and apply the formula (sum all new weights for all theme entities) */
        double themeEntityCase = 0.0;

        for (Entity e : themeE) {
            themeEntityCase += weightAdjuster.getNewWeight_ThemeEntity(e, docId, maxDocId);
        }


        /** treat the added entities cases:
         * case1: added entity belongs to RDi-1 (previously retrieved relevant docs)
         * case2: added entity DOESN'T belong to RDi-1 (previously retrieved relevant docs)
         * and apply the formula (sum all new weights for each case)  */
        double addedEntityCase1 = 0.0;
        double addedEntityCase2 = 0.0;

        for (Entity e : addedE) {
            /** if the entity occurs in RDi-1*/
            if (mysqlMetricsProvider.checkEntityOccurrence(e.getMention(), RDids)) {
                addedEntityCase1 += weightAdjuster.getNewWeight_AddedEntity1(e, docId, maxDocId);
            } else {
                //addedEntityCase2 += weightAdjuster.getNewWeight_AddedEntity2(e ,docId, maxDocId);
            }
        }

        /** treat the removed entities case and apply the formula (sum all new weights for all removed entities) */
        double removedEntityCase = 0.0;

        for (Entity e : removedE) {
            removedEntityCase += weightAdjuster.getNewWeight_RmEntity(e, docId, maxDocId);
        }

        /**Overall relevance score between query and document after RL weight adjustment */
        newScore = currentScore + A * themeEntityCase - B * addedEntityCase1 + C * addedEntityCase2 - D * removedEntityCase;

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
        currentScore = Math.log10(currentScore);

        return currentScore;
    }

}
