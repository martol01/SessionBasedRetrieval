package com.ucl.search.sbr.domain;

import com.google.gson.Gson;
import com.ucl.search.sbr.services.entityExtraction.Entity;
import com.ucl.search.sbr.services.entityExtraction.Interaction;
import com.ucl.search.sbr.services.entityExtraction.Session;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class EntityInteraction {

    private Session[] sessions;

    public EntityInteraction() {

        String pathToFile ="freebaseEntities.json";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(pathToFile).getFile());

        Gson gson = new Gson();
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(file));
            //convert the json string back to object
            sessions = gson.fromJson(br, Session[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Session[] getSessions(){
        return this.sessions;
    }

}
