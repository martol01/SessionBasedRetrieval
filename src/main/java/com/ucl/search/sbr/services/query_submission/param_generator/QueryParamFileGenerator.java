package com.ucl.search.sbr.services.query_submission.param_generator;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.ucl.search.sbr.domain.QueryEntity;
import com.ucl.search.sbr.domain.exception.GenerateParamXmlFileException;
import org.w3c.dom.Document;

import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Class responsible for generating the XML parameter file for the Indri query.
 *
 * Created by Gabriel on 2/11/2015.
 */
public class QueryParamFileGenerator {

    private static final String XML_FILE_TEMPLATE =
            "<parameters>" +
                "<index>%s</index>" +
                "<query>" +
                    "<text>%s</text>" +
                "</query>" +
                "<memory>%s</memory>" +
                "<count>%d</count>" +
                "<trecFormat>%b</trecFormat>" +
            "</parameters>";

    private String indexDirectory;
    private String memoryLimit;
    private int resultCount;
    private boolean enabledTrecFormat;

    private File tempDirectory;

    /**
     * Create a new instance of QueryParamFileGenerator. This newly-created instance
     * will generate files according to the constructor arguments.
     *
     * @param indexDirectory the absolute path to the directory where data is indexed.
     *                       Instructs Indri to use this index when performing the search
     * @param memoryLimit upper limit of the memory used by Indri when running the query.
     *                    Example value: "256M". Use K for kilobytes, M for megabytes and
     *                    G for gigabytes
     * @param resultCount upper limit of the number of returned results
     * @param enabledTrecFormat specify if you want Indri to return the results in TREC format
     */
    public QueryParamFileGenerator(String indexDirectory, String memoryLimit, int resultCount, boolean enabledTrecFormat) {
        this.indexDirectory = indexDirectory;
        this.memoryLimit = memoryLimit;
        this.resultCount = resultCount;
        this.enabledTrecFormat = enabledTrecFormat;

        this.tempDirectory = new File(System.getProperty("java.io.tmpdir"));
    }

    /**
     * Generate a XML parameter file for a Indri query.
     *
     * @param queryEntities entities that have been expanded
     * @return the newly-generated xml file
     */
    public File generateParamFile(List<QueryEntity> queryEntities) {
        // first, create a new, unique XML file
        File paramFile = generateUniqueFile();

        // create the XML document for the param file
        String document = generateParamDocument(queryEntities);

        // finally, output the XML document to the file on disk
        writeDocumentToFile(document, paramFile);
        return paramFile;
    }

    // create empty, temporary XML file with unique name. This file will be used for
    private File generateUniqueFile() {
        try {
            return File.createTempFile("param", ".xml", tempDirectory);
        } catch (IOException exception) {
            throw new GenerateParamXmlFileException("Error creating unique param file", exception);
        }
    }

    @VisibleForTesting
    String generateParamDocument(List<QueryEntity> queryEntities) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("#combine( ");
        for(QueryEntity entity : queryEntities) {
            queryBuilder.append(entity.getQueryText()).append(" ");
        }
        queryBuilder.append(")");

        return String.format(XML_FILE_TEMPLATE, indexDirectory, queryBuilder.toString(),
                memoryLimit, resultCount, enabledTrecFormat);
    }

    // write the generated XML document to the file
    private void writeDocumentToFile(String document, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(document);
            writer.flush();
        } catch (IOException ioException) {
            throw new GenerateParamXmlFileException("I/O Exception thrown when writing XML document to file", ioException);
        }
    }

}
