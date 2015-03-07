package com.ucl.search.sbr.services.transition_model_builder;


import com.ucl.search.sbr.domain.EntityInteraction;
import com.ucl.search.sbr.services.entityExtraction.Interaction;
import com.ucl.search.sbr.services.entityExtraction.Session;


/**
 * Created by ralucamelon on 05/03/2015.
 */
public class EntityTypeExtractor {


    public void extractEntityTheme()
    {
        EntityInteraction entInteraction = new EntityInteraction();
        Session[] sessions = entInteraction.getSessions();

        System.out.println(sessions.length);

        for (Session session:sessions){
            Interaction[] interactions = session.getInteractions();
            for(Interaction interaction: interactions){
                entInteraction.printEntitiesForInteraction(interaction);
            }
        }

    }

}
