package com.ucl.search.sbr.services.query_submission.search_call;

import com.ucl.search.sbr.testutils.TestUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestQuerySubmitter {

    QuerySubmitter querySubmitter = new QuerySubmitter();

    @Test
    public void testSubmitQuery() throws IOException {
        File paramFile = TestUtils.getResourceFile("param_file.xml");
        File resultFile = querySubmitter.submitQuery(paramFile);
        String resultText = new String(Files.readAllBytes(Paths.get(resultFile.toURI())), "UTF-8");
        System.out.printf("Result text:\n%s\n", resultText);
    }

}