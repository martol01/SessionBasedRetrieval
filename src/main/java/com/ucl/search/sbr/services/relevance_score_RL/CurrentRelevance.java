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

    public CurrentRelevance(MysqlEntityMetricsProvider entityMetricsProvider) {
        this.entityMetricsProvider = entityMetricsProvider;
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
    public double calculateCurrentRelevance(Interaction query, String docId, boolean simpleMLEflag) {

        /**
         * used to store the probabilities calculated with maximum likehood estimation with dirichlet smoothing parameter
         * -> one probability for each entity in the entity representation of a query
         **/
        List<Double> probabilities = new ArrayList<>();

        /** get the entity representation for the current query **/
        Entity[] entities = query.getEntities();

        /**
         *  for each entity in the entity representation of the query: e1, e2, e3, e4 and e5
         *  calculate P(e|d) and store the value in the 'probabilities' array
         *  -> probabilities will contain = {P(e1|d), P(e2|d), P(e3|d), P(e4|d), P(e5|d)}
         **/

       // System.out.println("entities in product probability: ");
        for (Entity e : entities) {
           // System.out.println(e.getMention() + "  " + e.getMid() + "  " + docId);

            if (simpleMLEflag == false) {
               // System.out.println("in MLE false");
                probabilities.add(getEntityProbabilityDoc(entityMetricsProvider.getEntityCorpusCount(e.getMid()),
                        entityMetricsProvider.getEntityDocumentCount(e.getMid(), docId), entityMetricsProvider.getDocumentLength(docId)));
            } else {
//                System.out.println("in MLE true");
//                System.out.println("doc id: " + docId);
//                System.out.println(entityMetricsProvider.getEntityDocumentCount(e.getMid(), docId) + "  " + entityMetricsProvider.getDocumentLength(docId));
                probabilities.add(getEntityProbabilityDoc(entityMetricsProvider.getEntityDocumentCount(e.getMid(), docId),
                        entityMetricsProvider.getDocumentLength(docId)));
            }
        }

        /** go through the 'probabilities' array and calculate P(qi|d) = current reward **/
        double probabilityProduct = 1.0;
        for (Double prob : probabilities) {
         //   System.out.println("probabilities list that need to be multiplied: " + prob);

            probabilityProduct *= (1.0 - prob);
        }

//        System.out.println("prob product is P(qi,d) = " + probabilityProduct);
//        System.out.println("returned val is (1-P(qi,d)) = " + (1.0-probabilityProduct));

        return (1.0 - probabilityProduct);
    }

    /**
     * calculates P(e|d) = (#(e|d) + DIRICHLET_PARAM * P(e|C)) / (|d| + DIRICHLET_PARAM) *
     */
    public double getEntityProbabilityDoc(double corpusP, long entityOccurence, long docLen) {

        if(docLen == 0)
            return 0;

        return ((double) entityOccurence + DIRICHLET_PARAM * corpusP) / ((double) docLen + DIRICHLET_PARAM);
    }

    /**
     * calculates MLE probability in the simple way (doesn't take into account the corpus data)
     * P(e|d) = #(e|d) / |d|
     * used for extracting the ideal document for each query in the session *
     */
    public double getEntityProbabilityDoc(long entityOccurence, long docLen) {

      //  System.out.println(entityOccurence + "  leng: " + docLen);

        if(docLen == 0)
            return 0;

        return ((double) entityOccurence) / ((double) docLen);
    }

    /**
     * returns the simple P(e|d) = #(e|d) / |d|
     *
     * @param e the entity whose probability needs to be calculated
     * @param docId document id
     * @return the new probability value P(e|d) calculated with maximum likelihood estimation
     * */
    public double getEntityProbabilityDoc(Entity e, String docId) {
        //  System.out.println("simple P(e|d) mle: " + result);
        double weight = getEntityProbabilityDoc(entityMetricsProvider.getEntityDocumentCount(e.getMid(), docId), entityMetricsProvider.getDocumentLength(docId));
        /* treat the case when Math.log(0.0) gives -Infinity */
        if (weight == 0.0)
            return 1;
        return weight;
    }
}
