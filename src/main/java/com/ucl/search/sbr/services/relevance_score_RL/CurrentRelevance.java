package com.ucl.search.sbr.services.relevance_score_RL;

import com.ucl.search.sbr.services.entityDb.MysqlEntityMetricsProvider;
import com.ucl.search.sbr.services.entityExtraction.Entity;
import com.ucl.search.sbr.services.entityExtraction.Interaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ralucamelon on 14/03/2015.
 */
public class CurrentRelevance {

    private final int DIRICHLET_PARAM = 5000;
    private MysqlEntityMetricsProvider entityMetricsProvider;
    private final String host = "localhost";
    private final String username = "root";
    private final String password = "";


    public CurrentRelevance() {
        try {
            this.entityMetricsProvider = new MysqlEntityMetricsProvider(host, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the current reward(relevance) between query and document
     * This method will be called for each query and each document from the
     * 1000 retrieved by the basic indri retrieval system
     *
     * @param query         the query under evaluation
     * @param docId         the id of the document under evaluation
     * @param simpleMLEflag differentiates between the score calculation formula; defines how P(e|d) is calculated
     *                      simpleMLEflag: (true -> simple MLE; false -> score includes corpus information)
     * @return current reward
     */

    public long getSomeCount(){
        System.out.println(entityMetricsProvider.getEntityText("/m/0100n9"));
        return entityMetricsProvider.getEntityCorpusCount("/m/0100n9");
    }

    public double calculateCurrentRelevance(Interaction query, String docId, boolean simpleMLEflag) {

        /**
         * used to store the probabilities calculated with maximum likehood estimation with dirichlet smoothing parameter
         * -> one probability for each entity in the entity representation of a query
         **/

        List<Double> probabilities = new ArrayList<>();
        double probabilityProduct = 1.0;

        /** get the entity representation for the current query **/
        Entity[] entities = query.getEntities();

        /**
         *  for each entity in the entity representation of the query: e1, e2, e3, e4 and e5
         *  calculate P(e|d) and store the value in the 'probabilities' array
         *  -> probabilities will contain = {P(e1|d, P(e2|d), P(e3|d), P(e4|d), P(e5|d)}
         **/

        for (Entity e : entities) {
            /* change here to get the entity id before calling the function */
            if (simpleMLEflag == false) {
                probabilities.add(getEntityProbabilityDoc(entityMetricsProvider.getEntityCorpusCount("/m/01007s"), entityMetricsProvider.getEntityDocumentCount("/m/01007s", docId), entityMetricsProvider.getDocumentLength(docId)));
            } else {
                // probabilities.add(getEntityProbabilityDoc(getEntityDocumentCount(e.id, docId), getDocumentLength(docId)));
            }

        }

        /** go through the 'probabilities' array and calculate P(qi|d) = current reward **/
        for (Double prob : probabilities) {
            probabilityProduct *= (1.0 - prob);
        }

        return (1.0 - probabilityProduct);
    }

    /**
     * calculates P(e|d) = (#(e|d) + DIRICHLET_PARAM * P(e|C)) / (|d| + DIRICHLET_PARAM) *
     */
    public double getEntityProbabilityDoc(double corpusP, long entityOccurence, long docLen) {

        return ((double) entityOccurence + DIRICHLET_PARAM * corpusP) / ((double) docLen + DIRICHLET_PARAM);
    }

    /**
     * calculates MLE probability in the simple way (doesn't take into account the corpus data)
     * P(e|d) = #(e|d) / |d|
     * used for extracting the ideal document for each query in the session *
     */
    public double getEntityProbabilityDoc(long entityOccurence, long docLen) {

        return ((double) entityOccurence) / ((double) docLen);
    }
}
