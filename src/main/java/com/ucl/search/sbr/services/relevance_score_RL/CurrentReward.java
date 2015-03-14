package com.ucl.search.sbr.services.relevance_score_RL;

import com.ucl.search.sbr.services.entities_frequency.CluewebDocument;
import com.ucl.search.sbr.services.entities_frequency.CluewebEntity;
import com.ucl.search.sbr.services.entities_frequency.CluewebInteraction;
import com.ucl.search.sbr.services.entityExtraction.Entity;
import com.ucl.search.sbr.services.entityExtraction.Interaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ralucamelon on 10/03/2015.
 */
public class CurrentReward {

    private final int DIRICHLET_PARAM = 5000;

    public CurrentReward(){}

    /*
    * calculates the current reward/relevance between query and document
    *
    * @params
    *
    * query : query under evaluation in the form of Interaction
    * docId : id of the document under evaluation
    *
    *
    * */

     public double calculateCurrentReward(Interaction query, String docId){

        CluewebInteraction interaction = new CluewebInteraction();
        CluewebDocument[] documents = interaction.getDocuments();
        CluewebEntity[] corpusEntities = interaction.getCorpusEntities();

        /* used to store the probabilities calculated with maximum likehood estimation with dirichlet smoothing parameter
        * -> one probability for each entity in the entity representation of a query
        * */
        List<Double> probabilities = new ArrayList<>();

        double probabilityProduct = 1.0;

        /* get the entity representation for the current query (interaction) */
        Entity[] entities = query.getEntities();

//        for(Entity e : entities){
//            System.out.println(e.getMention());
//        }

        /* go through all the documents and find the one with the right id */
        for (CluewebDocument doc : documents) {

            /* check if the document is the one needed for the reward calculation */
            if(doc.getDocid().equals(docId)){

                /* get the entities for the document */
                CluewebEntity[] docEntities = doc.getEntities();

                /* for each entity in the entity representation of the query: e1, e2, e3, e4 and e5
                *  calculate P(entity|d) and store the value in the 'probabilities' array
                *  -> probabilities will contain = {P(e1|d, P(e2|d), P(e3|d), P(e4|d), P(e5|d)}
                *
                * */

                 for(Entity e : entities){
                     probabilities.add(getEntityProbabilityDoc(getEntityProbabCorpus(e,corpusEntities), getEntityOccurence(e,docEntities), getDocLength(docId, documents)));
                }

                break;
            }
        }

        /* go through the 'probabilities' array and calculate P(qi|d) = current reward */
        for(Double prob : probabilities){
            probabilityProduct *= (1.0-prob);
        }

        return (1.0 - probabilityProduct);
    }

    /* calculates P(entity|d) = (#(entity|d) + DIRICHLET_PARAM * P(entity|C)) / (|d| + DIRICHLET_PARAM) */
    public double getEntityProbabilityDoc(double corpusP, int entityOccurence, int docLen){

        return ((double) entityOccurence + DIRICHLET_PARAM * corpusP)/( (double) docLen + DIRICHLET_PARAM);
    }



    /* calculates P(entity|C) = #(entity|C) / |C| */
    public double getEntityProbabCorpus(Entity entity, CluewebEntity[] corpusEntities){


        double entityProb;
        int totalEntitiesCorpus = 0;
        int entityOccurences = 0;

        for(CluewebEntity e: corpusEntities){

                totalEntitiesCorpus += Integer.parseInt(e.getValue());
                if(entity.getMention().equals(e.getMention())){
                    entityOccurences = Integer.parseInt(e.getValue());
                }
        }

        entityProb = ((double) entityOccurences)/totalEntitiesCorpus;
        return entityProb;

    }


    public int getEntityOccurence(Entity e,  CluewebEntity[] docEntities){

        int occurence = 0;

        for(CluewebEntity entity : docEntities){
            if(e.getMention().equals(entity.getMention())){
                occurence = Integer.parseInt(entity.getValue());
            }
        }

        return occurence;
    }


    /* calculate the document length = total nb of entities in the document */
    public int getDocLength(String docId, CluewebDocument[] documents){

        int docLen = 0;

        for(CluewebDocument d : documents){
            if(d.getDocid().equals(docId)){

                CluewebEntity[] entitiesInDoc = d.getEntities();

                for(CluewebEntity e : entitiesInDoc){
                    docLen += Integer.parseInt(e.getValue());
                }
            }
        }


        //System.out.println("lenth of doc: " + docId + " : " + docLen);
        return docLen;
    }
}
