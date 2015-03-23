package com.ucl.search.sbr.channels.main;


import com.ucl.search.sbr.domain.EntityInteraction;
import com.ucl.search.sbr.services.entityDb.MysqlEntityMetricsProvider;
import com.ucl.search.sbr.services.entityExtraction.Entity;
import com.ucl.search.sbr.services.entityExtraction.Interaction;
import com.ucl.search.sbr.services.entityExtraction.Session;
import com.ucl.search.sbr.services.maximum_reward.MaxRewardDocExtractor;
import com.ucl.search.sbr.services.query_submission.QuerySubmitter;
import com.ucl.search.sbr.services.relevance_score_RL.CurrentRelevance;
import com.ucl.search.sbr.services.relevance_score_RL.OverallRelevanceScore;
import com.ucl.search.sbr.services.transition_model_builder.EntityTypeExtractor;
import com.ucl.search.sbr.services.transition_model_builder.WeightAdjustment;
import lemurproject.indri.ParsedDocument;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main class - fires up all the crap.
 * <p/>
 * Created by Martin on 04/02/2015.
 */
public class EntryPoint {

    private static final String host = "localhost";
    private static final String username = "root";
    private static final String password = "gogaie";


    public static void main(String args[]) {

        MysqlEntityMetricsProvider mysqlMetricsProvider = null;
        try {
            mysqlMetricsProvider = new MysqlEntityMetricsProvider(host, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        EntityInteraction entInteraction = new EntityInteraction();
        Session[] sessions = entInteraction.getSessions();
        EntityTypeExtractor extractor = new EntityTypeExtractor();
        MaxRewardDocExtractor maxRewardExtractor = new MaxRewardDocExtractor(mysqlMetricsProvider);
        OverallRelevanceScore overallScore = new OverallRelevanceScore(mysqlMetricsProvider);
        CurrentRelevance currentWeight = new CurrentRelevance(mysqlMetricsProvider);
        WeightAdjustment weightAdjuster = new WeightAdjustment(mysqlMetricsProvider);
        QuerySubmitter querySubmitter = new QuerySubmitter();


        /** go through all the sessions */
        for (Session session : sessions) {
            System.out.println("Session id: " + session.getId());
            Interaction[] interactions = session.getInteractions();

            /** stores (docId, <entityId, entityWeight in doc with docIds>); for each session we initialize a new hashMap(inside)*/
            Map<String, HashMap<String, Double>> entityWeight = new HashMap<>();

            /** stores (queryId, <docId, score(q,d)>); for each session initialise a new HashMap(inside one) */
//            Map<String, HashMap<String, Double>> overallScorePerQuery = new HashMap<>();

            System.out.println("nb de queries: " + interactions.length);
            /* check if it makes sense to calculate theme, added and removed entities */
            if (interactions.length > 1) {

                for (int i = 0; i < interactions.length - 1; i++) {

                    Entity[] Eq1 = interactions[i].getEntities();

                    HashMap<String, Double> queryDocScore = maxRewardExtractor.buildQueryDocScore(interactions[i]);
                    /** get the id of the max rewarding document */
                    String maxRewardId = maxRewardExtractor.getMaxRewardingDoc(queryDocScore);
                    /** get the ids for the RD  */
                    ArrayList<String> RDids = maxRewardExtractor.getRDids(queryDocScore);

                    /* calculate the theme, added and removed entities for each pair of queries */
                    List<Entity> themeE = extractor.extractThemeEntities(interactions[i], interactions[i + 1]);
                    List<Entity> addedE = extractor.extractAddedEntities(interactions[i], interactions[i + 1]);
                    List<Entity> removedE = extractor.extractRemovedEntities(interactions[i], interactions[i + 1]);

                    ParsedDocument[] results = querySubmitter.getResultsForQuery(interactions[i + 1].getQuery(), 10);

                    /* for each document in results compute P(e|d) and store it in the HashMap for the correct docId
                    * if and only if that entity doesn't appear in the HashTable */
                    for (ParsedDocument doc : results) {
                        String docId = new String((byte[]) doc.metadata.get("docno"));
                        HashMap<String, Double> value = entityWeight.get(docId);

                        if (value == null) {
                            value = new HashMap<>();
                            entityWeight.put(docId, value);
                        }

                        /* if it is the first query calculate P(e|d) for each document */
                        if (i == 0) {

                            for (Entity e : Eq1) {
                                // System.out.println("entities in first query in session: " + e.getMid() + "   " + e.getMention());
                                double entWeight = currentWeight.getEntityProbabilityDoc(e, docId);

                                /* treat the case when Math.log(0.0) gives -Infinity */
                                if (entWeight == 0.0) {
                                    value.put(e.getMid(), 0.0);
                                } else {
                                    value.put(e.getMid(), Math.log(currentWeight.getEntityProbabilityDoc(e, docId)));
                                }
                            }
                        }


                        /**
                         * THEME ENTITIES WEIGHT ADJUSTMENT AND STATS UPDATING
                         * */
                        // System.out.println();
                        // System.out.println("the theme entities are: ");

                        for (Entity e : themeE) {
                            //  System.out.println(e.getMention() + "   " + e.getMid());

                            /* checks if the theme entity appears in the hashtable */
                            if (entityWeight.get(docId).containsKey(e.getMid())) {

                                // adjust the weight for the theme entities case
                                double newWeight = weightAdjuster.getNewWeight_ThemeEntity(e, maxRewardId,
                                        entityWeight.get(docId).get(e.getMid()));
                                // update the hashTable with the new weight
                                entityWeight.get(docId).put(e.getMid(), newWeight);

                            } else {
                                /* HashMap doesn't contain the entity Id -> calculate the weight for the entity */
                                double entWeight = currentWeight.getEntityProbabilityDoc(e, docId);

                                /* treat the case when Math.log(0.0) gives -Infinity */
                                if (entWeight == 0.0) {
                                    entityWeight.get(docId).put(e.getMid(), 0.0);
                                } else {
                                    entityWeight.get(docId).put(e.getMid(), Math.log(currentWeight.getEntityProbabilityDoc(e, docId)));
                                }

                            }
                        }

                        // System.out.println();
                        // System.out.println("the added entities are: ");

                        for (Entity e : addedE) {
                            // System.out.println(e.getMention() + "   " + e.getMid());

                            /** if the added entity belongs to RDi-1 (previously relevant documents) */
                            if (mysqlMetricsProvider.checkEntityOccurrence(e.getMid(), RDids) == true) {
                                if (entityWeight.get(docId).containsKey(e.getMid())) {

                                    double newWeight = weightAdjuster.getNewWeight_AddedEntity1(e, maxRewardId,
                                            entityWeight.get(docId).get(e.getMid()));
                                    entityWeight.get(docId).put(e.getMid(), newWeight);

                                } else {

                                    double entWeight = currentWeight.getEntityProbabilityDoc(e, docId);

                                /* treat the case when Math.log(0.0) gives -Infinity */
                                    if (entWeight == 0.0) {
                                        entityWeight.get(docId).put(e.getMid(), 0.0);
                                    } else {
                                        entityWeight.get(docId).put(e.getMid(), Math.log(currentWeight.getEntityProbabilityDoc(e, docId)));
                                    }

                                }
                            }
                            /** if the added entity DOESN'T belong to RDi-1 (previously relevant documents) */
                            else {

                                if (entityWeight.get(docId).containsKey(e.getMid())) {

                                    double newWeight = weightAdjuster.getNewWeight_AddedEntity2(e, mysqlMetricsProvider.getEntityIdf(e.getMid()),
                                            maxRewardId, entityWeight.get(docId).get(e.getMid()));
                                    entityWeight.get(docId).put(e.getMid(), newWeight);

                                } else {
                                    double entWeight = currentWeight.getEntityProbabilityDoc(e, docId);

                                /* treat the case when Math.log(0.0) gives -Infinity */
                                    if (entWeight == 0.0) {
                                        entityWeight.get(docId).put(e.getMid(), 0.0);
                                    } else {
                                        entityWeight.get(docId).put(e.getMid(), Math.log(currentWeight.getEntityProbabilityDoc(e, docId)));
                                    }

                                }
                            }
                        }


                        //  System.out.println();
                        // System.out.println("the removed entities are: ");

                        for (Entity e : removedE) {
                            //  System.out.println(e.getMention() + "   " + e.getMid());

                            if (entityWeight.get(docId).containsKey(e.getMid())) {

                                double newWeight = weightAdjuster.getNewWeight_RmEntity(e, maxRewardId,
                                        entityWeight.get(docId).get(e.getMid()));
                                entityWeight.get(docId).put(e.getMid(), newWeight);

                            } else {

                                double entWeight = currentWeight.getEntityProbabilityDoc(e, docId);

                                /* treat the case when Math.log(0.0) gives -Infinity */
                                if (entWeight == 0.0) {
                                    entityWeight.get(docId).put(e.getMid(), 0.0);
                                } else {
                                    entityWeight.get(docId).put(e.getMid(), Math.log(currentWeight.getEntityProbabilityDoc(e, docId)));
                                }

                            }
                        }
                    }


                    /* calculate the overall relevance score between (i+1)th query and each document
                     * and treat the case when i == 0 (first query in a session) */

                    HashMap<String, Double> scoreQueryDoc = new HashMap<>();
                    for (ParsedDocument doc : results) {
                        String docId = new String((byte[]) doc.metadata.get("docno"));
                        scoreQueryDoc.put(docId, overallScore.getNewScore(interactions[i + 1], docId,
                                themeE, addedE, removedE, entityWeight.get(docId)));
                    }

//                    overallScorePerQuery.put(interactions[i + 1].getNum(), scoreQueryDoc);

                    if (i == 0) {
                        scoreQueryDoc = new HashMap<>();

                        for (ParsedDocument doc : results) {
                            String docId = new String((byte[]) doc.metadata.get("docno"));
                            scoreQueryDoc.put(docId, overallScore.getNewScore(interactions[0], docId));
                        }

                       /* populate first entry corresponding to first query with the scores for each doc */
//                        overallScorePerQuery.put(interactions[0].getNum(), scoreQueryDoc);
                    }
                }
            } else {

                System.out.println("in else: here the session has only one query");

                Entity[] entities = interactions[0].getEntities();
                ParsedDocument[] results = querySubmitter.getResultsForQuery(interactions[0].getQuery(), 10);

                /* for each document in results compute P(e|d) and store it in the HashMap for the correct docId */
                for (ParsedDocument doc : results) {
                    String docId = new String((byte[]) doc.metadata.get("docno"));
                    HashMap<String, Double> value = entityWeight.get(docId);

                    if (value == null) {
                        value = new HashMap<>();
                        entityWeight.put(docId, value);
                    }

                    for (Entity e : entities) {
                        System.out.println("entities in only query in session: " + e.getMid() + "   " + docId);
                        double entWeight = currentWeight.getEntityProbabilityDoc(e, docId);

                        /* treat the case when Math.log(0.0) gives -Infinity */
                        if (entWeight == 0.0) {
                            entityWeight.get(docId).put(e.getMid(), 0.0);
                        } else {
                            entityWeight.get(docId).put(e.getMid(), Math.log(currentWeight.getEntityProbabilityDoc(e, docId)));
                        }
                    }

                }

                // just printing the values -- delete this part later
//                for (String key : entityWeight.keySet()) {
//                    for (String entityId : entityWeight.get(key).keySet()) {
//                        System.out.println( "map from docId -> (entityId, weightOfEntity)" + entityId + " ___ " + entityWeight.get(key).get(entityId));
//                    }
//                }

//                HashMap<String, Double> scoreQueryDoc = new HashMap<>();
//
//                for (ParsedDocument doc : results) {
//                    String docId = new String((byte[]) doc.metadata.get("docno"));
//                    scoreQueryDoc.put(docId, overallScore.getNewScore(interactions[0], docId));
//                }

                /* populate first entry corresponding to first query with the scores for each doc */
//                overallScorePerQuery.put(interactions[0].getNum(), scoreQueryDoc);
            }

            entityWeight.clear();
        }
    }

}
