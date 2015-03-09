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

    public CluewebEntity[] getCorpusEntities(){
        String pathToFile ="/home/martin/SessionBasedRetrieval/src/main/resources/corpus_entities_frequency.json";
        Gson gson = new Gson();
        CluewebEntity[] entities = null;
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(pathToFile));
            //convert the json string back to object
            entities = gson.fromJson(br, CluewebEntity[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entities;
    }
}
