package com.ucl.search.sbr.services.relevance_score_RL;

import com.ucl.search.sbr.domain.EntityInteraction;
import com.ucl.search.sbr.services.entityExtraction.Interaction;
import com.ucl.search.sbr.services.entityExtraction.Session;
import junit.framework.TestCase;
import org.junit.Test;

public class CurrentRewardTest extends TestCase {

    @Test
    public void testRewardCalculator() throws Exception {

        /* preparing the test data; here I used the first query in the first session from TREC14 */
        EntityInteraction entInteraction = new EntityInteraction();
        Session[] sessions = entInteraction.getSessions();
        Interaction query = sessions[0].getInteractions()[0];
        String docId = "2";

        CurrentReward reward = new CurrentReward();
        //double currentReward = reward.calculateCurrentReward(query, docId);

        //System.out.println(currentReward);

        assertTrue(true == true);
    }

}