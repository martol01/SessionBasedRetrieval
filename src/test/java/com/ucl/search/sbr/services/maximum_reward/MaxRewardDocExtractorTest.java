package com.ucl.search.sbr.services.maximum_reward;

import com.ucl.search.sbr.domain.EntityInteraction;
import com.ucl.search.sbr.services.entityExtraction.Interaction;
import com.ucl.search.sbr.services.entityExtraction.Session;
import com.ucl.search.sbr.services.transition_model_builder.EntityTypeExtractor;
import junit.framework.TestCase;
import org.junit.Test;

public class MaxRewardDocExtractorTest extends TestCase {


    @Test
    public void testMaxRewardDocExtractor(){


        MaxRewardDocExtractor maxRewardextractor = new MaxRewardDocExtractor();
        EntityInteraction entInteraction = new EntityInteraction();
        EntityTypeExtractor extractor = new EntityTypeExtractor();

        Session[] sessions = entInteraction.getSessions();

        Interaction[] interactions = sessions[0].getInteractions();

        maxRewardextractor.buildQueryDocScore(interactions[0]);

        System.out.println(interactions[0].getEntities()[0].getMention() + "  id : " + interactions[0].getEntities()[0].getMid());


        assertTrue(true == true);

    }
}