package com.ucl.search.sbr.services.dexterEntityLinking;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by root on 13/02/15.
 */
public class DexterConnection {

    public StringBuffer connectToURL(String url, String method) throws IOException {
        URL urlObject = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlObject.openConnection();
        conn.setRequestMethod(method);
        int responseCode = conn.getResponseCode();
        //System.out.println("Sending "+method+"request to URL : " + url);
        //System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response;
    }

    public String prepareQuery(String query, double min_conf){
        System.out.println("ACTUAL QUERY: "+query);
        String[] words = query.split(" ");
        StringBuilder sb = new StringBuilder();
        sb.append(words[0]);
        for(int i=1; i< words.length; i++){
            sb.append("%20");
            sb.append(words[i]);
        }
        sb.append("%wn=true&debug=false&format=text&min-conf="+min_conf);
        return sb.toString();
    }

    public void extractEntityId(String url){
        StringBuffer response = null;
        try {
            response = connectToURL(url, "GET");
            System.out.println(response.toString());
            Gson gson = new Gson();
            Entity entity = gson.fromJson(String.valueOf(response),Entity.class);
            System.out.println("TITLE: "+entity.getTitle());
            System.out.println("ID: "+entity.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void extractSpots(String url){
        StringBuffer response = null;
        try {
            response = connectToURL(url, "GET");
            //System.out.println(response.toString());
            Gson gson = new Gson();
            AnnotateResponse res = gson.fromJson(String.valueOf(response), AnnotateResponse.class);
            Spot[] spots = res.getSpots();
            //System.out.println("Number of entities: "+spots.length);
            for (int i = 0; i < spots.length; i++){
                //System.out.println(spots[i].getMention());
            }
            //System.out.println();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
