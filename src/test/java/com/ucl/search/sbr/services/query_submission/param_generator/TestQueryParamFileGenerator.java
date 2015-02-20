package com.ucl.search.sbr.services.query_submission.param_generator;

import com.ucl.search.sbr.domain.QueryEntity;
import com.ucl.search.sbr.testutils.TestUtils;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TestQueryParamFileGenerator {

    private QueryParamFileGenerator paramFileGenerator;
    private List<QueryEntity> queryEntities;

    @Before
    public void init() {
        String indexFile = "/path/to/index/file";
        String memoryLimit = "256M";
        int resultCount = 50;
        boolean enabledTrecFormat = true;

        this.paramFileGenerator = new QueryParamFileGenerator(indexFile, memoryLimit, resultCount, enabledTrecFormat);
        this.queryEntities = Arrays.asList(
                new QueryEntity(1, "first_query_term"),
                new QueryEntity(.2, "second_query_term"),
                new QueryEntity(.5, "third_query_term")
        );
    }

    @Test
    public void testGenerateParamDocument() throws TransformerException, IOException, SAXException {
        String actualXmlString = paramFileGenerator.generateParamDocument(queryEntities);
        String expectedXmlString = TestUtils.readResourceTextFile("param_file.xml");

        XMLUnit.setIgnoreWhitespace(true);
        XMLAssert.assertXMLEqual(actualXmlString, expectedXmlString);
    }

}