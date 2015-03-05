package com.ucl.search.sbr.channels.main;


import com.ucl.search.sbr.domain.EntityInteraction;
import com.ucl.search.sbr.services.entityExtraction.Interaction;
import com.ucl.search.sbr.services.entityExtraction.Session;

/**
 * Created by Martin on 04/02/2015.
 */
public class EntryPoint {
    public static void main(String args[]) {
        System.out.println("Hello World!");
        EntityInteraction entInteraction = new EntityInteraction();
        Session[] sessions = entInteraction.getSessions();
        System.out.println(sessions.length);
//        for (Session session:sessions){
//            Interaction[] interactions = session.getInteractions();
//            for(Interaction interaction: interactions){
//                entInteraction.printEntitiesForInteraction(interaction);
//            }
//        }


    }
}
