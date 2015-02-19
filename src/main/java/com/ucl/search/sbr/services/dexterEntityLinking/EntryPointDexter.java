package com.ucl.search.sbr.services.dexterEntityLinking;

import com.ucl.search.sbr.services.XMLParser.DomParser;
import com.ucl.search.sbr.services.XMLParser.Interaction;
import com.ucl.search.sbr.services.XMLParser.Session;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

/**
 * Created by root on 13/02/15.
 */
public class EntryPointDexter {
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
                    List<Interaction> interactions = session.getInteractions();
                    for(Interaction interaction: interactions){
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
