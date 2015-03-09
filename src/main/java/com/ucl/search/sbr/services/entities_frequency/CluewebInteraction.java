package com.ucl.search.sbr.services.entities_frequency;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by root on 09/03/15.
 */
public class CluewebInteraction {

    public CluewebDocument[] getDocuments(){
        String pathToFile ="/home/martin/SessionBasedRetrieval/src/main/resources/doc_entities_frequency.json";
        Gson gson = new Gson();
        CluewebDocument[] docs = null;
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(pathToFile));
            //convert the json string back to object
            docs = gson.fromJson(br, CluewebDocument[].class);
            System.out.println("DOCS LENGTH: "+docs.length);
            for (int i = 0; i < docs.length; i++) {
                System.out.println(docs[i].getDocid());
                CluewebEntity[] entities = docs[i].getEntities();
                for (int j = 0; j < entities.length; j++) {
                    System.out.println(entities[j].getMention()+", "+entities[j].getValue());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return docs;
    }

    public CluewebDocument getEntitiesForDoc(CluewebDocument[] docs, String docid){
        for (int i = 0; i < docs.length; i++) {
            if(docs[i].getDocid().equals(docid)){
                return docs[i];
            }
        }
        return null;
    }
    public void getCorpusEntities(){
        String pathToFile ="/home/martin/SessionBasedRetrieval/src/main/resources/corpus_entities_frequency.json";
        Gson gson = new Gson();
        CluewebEntity[] entities;
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(pathToFile));
            //convert the json string back to object
            entities = gson.fromJson(br, CluewebEntity[].class);
            System.out.println("DOCS LENGTH: "+entities.length);
            for (int i = 0; i < entities.length; i++) {
                System.out.println(entities[i].getMention()+", "+entities[i].getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
