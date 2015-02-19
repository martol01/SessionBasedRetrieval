package com.ucl.search.sbr.services.XMLParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 18/02/15.
 */
public class DomParser {
    private List<Session> sessions = new ArrayList<Session>();
    public List<Session> parse(String filename) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(ClassLoader.getSystemResourceAsStream(filename));
        List<Session> sessionsList = new ArrayList<Session>();
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        System.out.println(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                Session session = new Session();
                session.setNum(node.getAttributes().getNamedItem("num").getNodeValue());
                session.setStarttime(node.getAttributes().getNamedItem("starttime").getNodeValue());
                session.setUserid(node.getAttributes().getNamedItem("userid").getNodeValue());
                NodeList childNodes = node.getChildNodes();
                //System.out.println("CHILD NODES: " + childNodes.getLength());
                //child 0 is always the topic, last child is always the currentquery
                //extractTopicData(childNodes.item(0));
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node cNode = childNodes.item(j);
                    if (cNode instanceof Element) {
                       checkTag(session, cNode, ((Element) cNode).getTagName());
                        //System.out.println(((Element) cNode).getTagName());
                    }
                }
                //System.out.println("AL LENGTH: "+session.getInteractions().size());
//                for (int j = 0; j < session.getInteractions().size(); j++) {
//                    System.out.println("iQUERY: "+session.getInteractions().get(j).getQuery());
//                }
                //System.out.println("session topic: "+session.getTopic().getDesc());
                //System.out.println("session curquery: "+session.getCurQuery().getQuery());
                sessionsList.add(session);
                break;
            }
        }
        return sessionsList;
    }

    public void checkTag(Session session, Node cNode, String tagName){
        //one topic and one current query per session, several interactions
        if(tagName.equals("topic")){
            session.setTopic(extractTopicData(cNode));
        } else if(tagName.equals("interaction")){
            //System.out.println("FOUND Interaction");
            List<Interaction> interactions = session.getInteractions();
            if(interactions == null){
                interactions = new ArrayList<Interaction>();
            }
            interactions.add(extractInteractionData(cNode));
        } else{
            session.setCurQuery(extractCurrentQuery(cNode));
            //System.out.println("FOUND CURQUERY");
        }
    }

    public CurrentQuery extractCurrentQuery(Node node){
        System.out.println("CUR QUERY");
        System.out.println(node);
        CurrentQuery curquery = new CurrentQuery();
        curquery.setStarttime(node.getAttributes().getNamedItem("starttime").getNodeValue());
        NodeList childNodes = node.getChildNodes();
        System.out.println(childNodes.getLength());
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node cNode = childNodes.item(i);
            if(cNode instanceof Element){
                System.out.println(cNode);
                String query = cNode.getLastChild().getTextContent().trim();
                curquery.setQuery(query);
            }
        }
        return curquery;
    }
    public void extractSessionData(NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                Session session = new Session();
                session.setNum(node.getAttributes().getNamedItem("num").getNodeValue());
                session.setStarttime(node.getAttributes().getNamedItem("starttime").getNodeValue());
                session.setUserid(node.getAttributes().getNamedItem("userid").getNodeValue());
                NodeList childNodes = node.getChildNodes();
                //Topic topic = extractTopicData(childNodes);
                //Interaction[] is = extractInteractions(childNodes);
                //CurrentQuery curQuery = extractCurrentQuery(childNodes);

            }
        }
    }

    public Interaction extractInteractionData(Node node){
        Interaction interaction = new Interaction();
        interaction.setNum(node.getAttributes().getNamedItem("num").getNodeValue());
        interaction.setStarttime(node.getAttributes().getNamedItem("starttime").getNodeValue());
        interaction.setType(node.getAttributes().getNamedItem("type").getNodeValue());
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node cNode = childNodes.item(i);
            if(cNode instanceof Element){
                System.out.println(cNode);
                if(((Element) cNode).getTagName().equals("query")){
                    String query = cNode.getLastChild().getTextContent().trim();
                    interaction.setQuery(query);
                }else{
                  checkInteractionChild(cNode, ((Element) cNode).getTagName());
                }
            }
        }
        return interaction;
    }

    public void checkInteractionChild(Node node, String tagName){
        if(tagName.equals("results")) {
          System.out.println("RESULTS");
        } else{
            System.out.println("CLICKD");
        }
    }

    public Topic extractTopicData(Node node){
        Topic topic = new Topic();
        topic.setNum(node.getAttributes().getNamedItem("num").getNodeValue());
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node cNode = childNodes.item(i);
            if(cNode instanceof Element){
                String desc = cNode.getLastChild().getTextContent().trim();
                topic.setDesc(desc);
            }
        }
        return topic;
    }

}