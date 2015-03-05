package com.ucl.search.sbr.domain;

import com.google.gson.Gson;
import com.ucl.search.sbr.services.entityExtraction.Entity;
import com.ucl.search.sbr.services.entityExtraction.Interaction;
import com.ucl.search.sbr.services.entityExtraction.Session;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by root on 04/03/15.
 */
public class EntityInteraction {
    private Gson gson;
    private Session[] sessions;

    public EntityInteraction(){
        String pathToFile ="/home/martin/SessionBasedRetrieval/src/main/resources/sessions.json";
        gson = new Gson();
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(pathToFile));
            //convert the json string back to object
            sessions = gson.fromJson(br, Session[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Session[] getSessions(){
        return this.sessions;
    }

    public Session getSession(int id){
        return sessions[id];
    }

    public Interaction[] getInteractionsForSession(int sessionId){
        Interaction[] interactions = sessions[sessionId].getInteractions();
        return interactions;
    }

    public Entity[] getEntitiesForInteraction(Interaction interaction){
        return interaction.getEntities();
    }

    public void printEntitiesForInteraction(Interaction interaction){
        Entity[] entities = interaction.getEntities();
        for (Entity entity:entities){
            System.out.println(entity.getMention());
        }
    }

}
