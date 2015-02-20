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
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                sessionsList.add(extractSessionData(node));
                break;
            }
        }
        return sessionsList;
    }

    public Session extractSessionData(Node node) {
        Session session = new Session();
        session.setNum(node.getAttributes().getNamedItem("num").getNodeValue());
        session.setStarttime(node.getAttributes().getNamedItem("starttime").getNodeValue());
        session.setUserid(node.getAttributes().getNamedItem("userid").getNodeValue());
        NodeList childNodes = node.getChildNodes();
        for (int j = 0; j < childNodes.getLength(); j++) {
           Node cNode = childNodes.item(j);
           if (cNode instanceof Element) {
               checkTag(session, cNode, ((Element) cNode).getTagName());
           }
        }
        return session;
    }

    public void checkTag(Session session, Node cNode, String tagName){
        //one topic and one current query per session, several interactions
        switch (tagName){
            case "topic":
                session.setTopic(extractTopicData(cNode));
                break;
            case "interaction":
                List<Interaction> interactions = session.getInteractions();
                if(interactions == null){
                    interactions = new ArrayList<Interaction>();
                }
                interactions.add(extractInteractionData(cNode));
                break;
            case "currentquery":
                session.setCurQuery(extractCurrentQuery(cNode));
                break;
        }
    }

    public CurrentQuery extractCurrentQuery(Node node){
        CurrentQuery curquery = new CurrentQuery();
        curquery.setStarttime(node.getAttributes().getNamedItem("starttime").getNodeValue());
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node cNode = childNodes.item(i);
            if(cNode instanceof Element){
                String query = cNode.getLastChild().getTextContent().trim();
                curquery.setQuery(query);
            }
        }
        return curquery;
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
                if(((Element) cNode).getTagName().equals("query")){
                    String query = cNode.getLastChild().getTextContent().trim();
                    interaction.setQuery(query);
                }else{
                  checkInteractionChild(interaction, cNode, ((Element) cNode).getTagName());
                }
            }
        }
        return interaction;
    }


    public void checkInteractionChild(Interaction interaction, Node node, String tagName){
        if(tagName.equals("results")) {
            interaction.setResults(extractResults(node));
        } else{
            interaction.setClicked(extractClicked(node));
        }
    }

    public List<ClickedResult> extractClicked(Node node){
        List<ClickedResult> clicked = new ArrayList<ClickedResult>();
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node cNode = childNodes.item(i);
            if(cNode instanceof Element){
                clicked.add(extractClick(cNode));
            }
        }
        return clicked;
    }

    public ClickedResult extractClick(Node node){
        ClickedResult click = new ClickedResult();
        click.setNum(node.getAttributes().getNamedItem("num").getNodeValue());
        click.setStarttime(node.getAttributes().getNamedItem("starttime").getNodeValue());
        click.setEndtime(node.getAttributes().getNamedItem("endtime").getNodeValue());
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength() ; i++) {
            Node cNode = childNodes.item(i);
            if(cNode instanceof Element){
                checkClickChildren(click, cNode, ((Element) cNode).getTagName());
            }
        }
        return click;
    }

    public void checkClickChildren(ClickedResult click, Node node, String tagName){
        switch (tagName){
            case "rank":
                click.setRank(node.getLastChild().getTextContent().trim());
                break;
            case "docno":
                click.setDocno(node.getLastChild().getTextContent().trim());
                break;
        }
    }


    public List<InteractionResult> extractResults(Node node){
        List<InteractionResult> results = new ArrayList<InteractionResult>();
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node cNode = childNodes.item(i);
            if(cNode instanceof Element){
                results.add(extractResult(cNode));
            }
        }
        return results;
    }

    public InteractionResult extractResult(Node node){

        InteractionResult result = new InteractionResult();
        result.setRank(node.getAttributes().getNamedItem("rank").getNodeValue());
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node cNode = childNodes.item(i);
            if(cNode instanceof Element){
               checkResultChildren(cNode, result, ((Element) cNode).getTagName());
            }
        }
        return result;
    }

    public void checkResultChildren(Node node, InteractionResult result, String tagName){
       switch (tagName){
           case "url":
               String url = node.getLastChild().getTextContent().trim();
               result.setUrl(url);
               break;
           case "clueweb12id":
               String clueweb = node.getLastChild().getTextContent().trim();
               result.setClueweb12id(clueweb);
               break;
           case "title":
               if(node.getLastChild()!=null) {
                   String title = node.getLastChild().getTextContent().trim();
                   result.setTitle(title);
               }
               break;
           case "snippet":
               if(node.getLastChild()!=null){
                   String snippet = node.getLastChild().getTextContent().trim();
                   result.setSnippet(snippet);
               }
               break;
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