package com.ucl.search.sbr.services.dexterEntityLinking;

import XMLParser.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

/**
 * Created by root on 13/02/15.
 */
public class EntryPoint {
    public static void main(String args[]){
        DexterConnection conn = new DexterConnection();
        String url = "http://localhost:8080/dexter-webapp/api/rest/get-id?title=Johnny_Cash";
        //conn.extractEntityId(url);
        String urlSpots = "http://localhost:8080/dexter-webapp/api/rest/annotate?text=Steven%20Gerrard%20joins%20Liverpool" +
                "%20after%20fifa%20World%20Cup&n=50&wn=true&debug=false&format=text&min-conf=0.5";
        //conn.extractSpots(urlSpots);
        DomParser parser = new DomParser();
        try {
            List<Session> sessions = parser.parse("sessiontrack2014.xml");
            if(sessions!= null){
                for(Session session: sessions){
                    //System.out.println("TOPIC DESC: "+session.getTopic().getDesc());
                    //System.out.println("Session curquery: "+session.getCurQuery());
                    List<Interaction> interactions = session.getInteractions();
                    for(Interaction interaction: interactions){
//                            System.out.println("Inter query: "+interaction.getQuery());
//                        for(InteractionResult res:interaction.getResults()){
//                            System.out.println("URL: "+res.getUrl());
//                            System.out.println("CLUEWEB: "+res.getClueweb12id());
//                            System.out.println("TITLE: "+res.getTitle());
//                            System.out.println("SNIPPET"+res.getSnippet());
//                        }
//                        if(interaction.getClicked()!=null){
//                            System.out.println("Interaction num="+interaction.getNum());
//                            for(ClickedResult click:interaction.getClicked()){
//                                System.out.println("CLICK RANK: "+click.getRank());
//                                System.out.println("CLICK DOCNO: "+click.getDocno());
//                            }
//                        }

                        String query = conn.prepareQuery(interaction.getQuery(), 0.5);//extract and sanitize query, pass query to Dexter Entity add properties
                        String urlAnnotate = "http://localhost:8080/dexter-webapp/api/rest/annotate?text=" + query;
                        conn.extractSpots(urlAnnotate);
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
