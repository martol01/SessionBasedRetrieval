package com.ucl.search.sbr.services.transition_model_builder;

import com.ucl.search.sbr.domain.EntityInteraction;
import com.ucl.search.sbr.services.entityExtraction.Interaction;
import com.ucl.search.sbr.services.entityExtraction.Session;
import junit.framework.TestCase;
import org.junit.Test;

public class EntityTypeExtractorTest extends TestCase {


    /* test if the theme, removed and added entities are correctly identified */
    @Test
    public void testEntityTypeExtraction() {

        EntityInteraction entInteraction = new EntityInteraction();
        Session[] sessions = entInteraction.getSessions();


        for (Session session:sessions){

            System.out.println();
            System.out.println("Session id: " + session.getId());
            Interaction[] interactions = session.getInteractions();
            System.out.println("session has: " + interactions.length + " queries.");


            for(Interaction interaction: interactions){
                System.out.println("entities for query: " + interaction.getQuery());
                entInteraction.printEntitiesForInteraction(interaction);
                System.out.println();
            }
        }

        assertTrue(sessions.length == 1257);



    }

}