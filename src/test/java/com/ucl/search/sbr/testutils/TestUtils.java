package com.ucl.search.sbr.testutils;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Gabriel on 2/13/2015.
 */
public class TestUtils {

    private static Transformer transformer;
    private static DocumentBuilder db;

    static {
        try {
            transformer = TransformerFactory.newInstance().newTransformer();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setCoalescing(true);
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setIgnoringComments(true);
            db = dbf.newDocumentBuilder();
        } catch (Exception e) {
            throw new RuntimeException("Error initialising TestUtils class", e);
        }
    }

    public static String readResourceTextFile(String path) {
        try {
            URL fileUrl = TestUtils.class.getClassLoader().getResource(path);
            Path resPath = Paths.get(fileUrl.toURI());
            return new String(Files.readAllBytes(resPath), "UTF-8");
        } catch (IOException | URISyntaxException | NullPointerException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static File getResourceFile(String filename) {
        try {
            URL fileUrl =  TestUtils.class.getClassLoader().getResource(filename);
            return new File(fileUrl.toURI());
        } catch (URISyntaxException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static Document xmlStringToDocument(String xmlString) {
        try {
            InputSource source = new InputSource(new StringReader(xmlString));
            Document doc = db.parse(source);
            doc.normalizeDocument();
            return doc;
        } catch (SAXException | IOException e) {
            throw new RuntimeException("Error parsing xml string", e);
        }
    }

    public static String xmlDocumentToString(Document document) {
        try {
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            return writer.getBuffer().toString();
        } catch (TransformerException e) {
            throw new RuntimeException("Error converting XML document to String", e);
        }
    }
}
