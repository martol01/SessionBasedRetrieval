package com.ucl.search.sbr.channels.main;


import com.ucl.search.sbr.domain.EntityInteraction;
import com.ucl.search.sbr.services.entities_frequency.CluewebDocument;
import com.ucl.search.sbr.services.entities_frequency.CluewebEntity;
import com.ucl.search.sbr.services.entities_frequency.CluewebInteraction;
import com.ucl.search.sbr.services.entityExtraction.Session;

/**
 * Created by Martin on 04/02/2015.
 */
public class EntryPoint {
    public static void main(String args[]) {
        System.out.println("Hello World!");
        CluewebInteraction interaction = new CluewebInteraction();
        CluewebDocument[] documents = interaction.getDocuments();
        System.out.println("DOCS LENGTH: "+documents.length);
        for (int i = 0; i < documents.length; i++) {
            System.out.println(documents[i].getDocid());
            CluewebEntity[] entities = documents[i].getEntities();
            for (int j = 0; j < entities.length; j++) {
                System.out.println(entities[j].getMention()+", "+entities[j].getValue());
            }
        }
        CluewebEntity[] entities = interaction.getCorpusEntities();
        for (int i = 0; i < entities.length; i++) {
            System.out.println(entities[i].getMention()+", "+entities[i].getValue());
        }
        System.out.println("TOTAL No of entities in corpus: "+entities.length);
        
//        EntityInteraction entInteraction = new EntityInteraction();
//        Session[] sessions = entInteraction.getSessions();
//        System.out.println(sessions.length);
//        for (Session session:sessions){
//            Interaction[] interactions = session.getInteractions();
//            for(Interaction interaction: interactions){
//                entInteraction.printEntitiesForInteraction(interaction);
//            }
//        }


    }
}
