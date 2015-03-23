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
    private static final String password = "";


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

       // Session session = sessions[1000];

        for (Session session : sessions) {
            System.out.println("Session id: " + session.getId());
            Interaction[] interactions = session.getInteractions();
            System.out.println(interactions.length + " nb of queries in session");

            /** stores (docId, <entityId, entityWeight in doc with docIds>); for each session we initialize a new hashMap(inside)*/
            Map<String, HashMap<String, Double>> entityWeight = new HashMap<>();

            /** stores (queryId, <docId, score(q,d)>); for each session initialise a new HashMap(inside one) */
            Map<String, HashMap<String, Double>> overallScorePerQuery = new HashMap<>();

            for (int i = 0; i < interactions.length; i++) {

                if (i == 0) {
                    System.out.println("first query");

                    Entity[] entities = interactions[0].getEntities();
                    ParsedDocument[] results = querySubmitter.getResultsForQuery(interactions[0].getEntityQuery(), 15);

                    /* for each document in results compute P(e|d) and store it in the HashMap for the correct docId */
                    for (ParsedDocument doc : results) {
                        String docId = new String((byte[]) doc.metadata.get("docno"));
                        entityWeight.put(docId, new HashMap<String, Double>());

                        for (Entity e : entities) {
                            double entWeight = currentWeight.getEntityProbabilityDoc(e, docId);
                            entityWeight.get(docId).put(e.getMid(), entWeight);
                        }

                    }

                    HashMap<String, Double> scoreQueryDoc = new HashMap<>();

                    for (ParsedDocument doc : results) {
                        String docId = new String((byte[]) doc.metadata.get("docno"));
                        scoreQueryDoc.put(docId, overallScore.getNewScore(interactions[0], docId));
                    }

                    System.out.println("-------------------------------");
                    for(String key: scoreQueryDoc.keySet()){
                        System.out.println(scoreQueryDoc.get(key));
                    }

                    /* populate first entry corresponding to first query with the scores for each doc */
                    overallScorePerQuery.put(interactions[0].getQuery(), scoreQueryDoc);
                } else {

                    HashMap<String, Double> queryDocScore = maxRewardExtractor.buildQueryDocScore(interactions[i-1]);
                    /** get the id of the max rewarding document */
                    String maxRewardId = maxRewardExtractor.getMaxRewardingDoc(queryDocScore);
                    /** get the ids for the RD */
                    ArrayList<String> RDids = maxRewardExtractor.getRDids(queryDocScore);

                    /* calculate the theme, added and removed entities for each pair of queries */
                    List<Entity> themeE = extractor.extractThemeEntities(interactions[i-1], interactions[i]);
                    List<Entity> addedE = extractor.extractAddedEntities(interactions[i-1], interactions[i]);
                    List<Entity> removedE = extractor.extractRemovedEntities(interactions[i-1], interactions[i]);

                    ParsedDocument[] results = querySubmitter.getResultsForQuery(interactions[i].getEntityQuery(), 15);

                    /* for each document in results compute P(e|d) and store it in the HashMap for the correct docId
                    * if and only if that entity doesn't appear in the HashTable */
                    for (ParsedDocument doc : results) {
                        String docId = new String((byte[]) doc.metadata.get("docno"));
                        HashMap<String, Double> value = entityWeight.get(docId);

                        if (value == null) {
                            value = new HashMap<>();
                            entityWeight.put(docId, value);
                        }

                        /**
                         * THEME ENTITIES WEIGHT ADJUSTMENT AND STATS UPDATING
                         * */
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
                                entityWeight.get(docId).put(e.getMid(), entWeight);
                            }
                        }

                        for (Entity e : addedE) {

                            /** if the added entity belongs to RDi-1 (previously relevant documents) */
                            if (mysqlMetricsProvider.checkEntityOccurrence(e.getMid(), RDids)) {
                                if (entityWeight.get(docId).containsKey(e.getMid())) {

                                    double newWeight = weightAdjuster.getNewWeight_AddedEntity1(e, maxRewardId,
                                            entityWeight.get(docId).get(e.getMid()));
                                    entityWeight.get(docId).put(e.getMid(), newWeight);

                                } else {
                                    double entWeight = currentWeight.getEntityProbabilityDoc(e, docId);
                                    entityWeight.get(docId).put(e.getMid(), entWeight);
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
                                    entityWeight.get(docId).put(e.getMid(), entWeight);
                                }
                            }
                        }

                        for (Entity e : removedE) {
                            if (entityWeight.get(docId).containsKey(e.getMid())) {
                                double newWeight = weightAdjuster.getNewWeight_RmEntity(e, maxRewardId,
                                        entityWeight.get(docId).get(e.getMid()));
                                entityWeight.get(docId).put(e.getMid(), newWeight);
                            } else {
                                double entWeight = currentWeight.getEntityProbabilityDoc(e, docId);
                                entityWeight.get(docId).put(e.getMid(), entWeight);
                            }
                        }
                    }

                    /* calculate the overall relevance score between i-th query and each document */

                    HashMap<String, Double> scoreQueryDoc = new HashMap<>();
                    for (ParsedDocument doc : results) {
                        String docId = new String((byte[]) doc.metadata.get("docno"));
                        scoreQueryDoc.put(docId, overallScore.getNewScore(interactions[i], docId,
                                themeE, addedE, removedE, entityWeight.get(docId)));
                    }

                    overallScorePerQuery.put(interactions[i].getQuery(), scoreQueryDoc);

                    System.out.println("-------------------------------");
                    for(String key: scoreQueryDoc.keySet()){
                        System.out.println(scoreQueryDoc.get(key));
                    }
                }
            }

            printResults(overallScorePerQuery);
            entityWeight.clear();
        }
    }

    private static void printResults(Map<String, HashMap<String, Double>> overallScorePerQuery) {
        for (String query : overallScorePerQuery.keySet()) {
            System.out.printf("Query: %s\n", query);
            System.out.printf("Retrieved documents: %s\n\n", overallScorePerQuery.get(query));
        }
    }
}
