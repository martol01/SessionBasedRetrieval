package com.ucl.search.sbr.services.transition_model_builder;

import com.ucl.search.sbr.services.entityExtraction.Entity;
import com.ucl.search.sbr.services.relevance_score_RL.CurrentRelevance;

/**
 * Created by ralucamelon on 15/03/2015.
 */
public class WeightAdjustment {

    private CurrentRelevance currentRelevance;

    public WeightAdjustment() {
        this.currentRelevance = new CurrentRelevance();
    }

    /**
     * Calculates the new weight for the theme entity
     *
     * @param e           entity under evaluation
     * @param docId       document id for the document under evaluation
     * @param maxRelDocId the id of the document with the max relevance from the previously 10 retrieved docs
     * @return the new weight for the theme entity
     */
    public double getNewWeight_ThemeEntity(Entity e, String docId, String maxRelDocId) {

        double newWeight;

        /* apply formula for weight increase for the theme entities */
        newWeight = Math.log10(currentRelevance.getEntityProbabilityDoc(e, docId)) * (1 - currentRelevance.getEntityProbabilityDoc(e, maxRelDocId));

        return newWeight;

    }

    /**
     * Gets the new weight for the removed entities
     */
    public double getNewWeight_RmEntity(Entity e, String docId, String maxRelDocId) {
        double newWeight;

         /* apply formula for weight decrease for the removed entities */
        newWeight = Math.log10(currentRelevance.getEntityProbabilityDoc(e, docId)) * (currentRelevance.getEntityProbabilityDoc(e, maxRelDocId));

        return newWeight;

    }

    /**
     * Gets the new weight for the added entities that belong to RDi-1 (previously relevant documents)
     */
    public double getNewWeight_AddedEntity1(Entity e, String docId, String maxRelDocId) {

        double newWeight;

         /* apply formula for weight decrease for the removed entities */
        newWeight = Math.log10(currentRelevance.getEntityProbabilityDoc(e, docId)) * (currentRelevance.getEntityProbabilityDoc(e, maxRelDocId));

        return newWeight;

    }

    /**
     * Gets the new weight for the added entities that DON'T belong to RDi-1 (previously relevant documents)
     */
    public double getNewWeight_AddedEntity2(Entity e, String docId, String maxRelDocId) {

        double newWeight = 0.0;

        return newWeight;

    }
}
