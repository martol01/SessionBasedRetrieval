package com.ucl.search.sbr.services.query_submission.search_call;

import com.ucl.search.sbr.domain.exception.IndriQueryException;

import java.io.File;
import java.io.IOException;

/**
 * This class handles the effective query to the Indri search engine.
 *
 * Created by Gabriel on 2/11/2015.
 */
public class QuerySubmitter {

    private static final String COMMAND_FORMAT = "IndriRunQuery \"%s\" > \"%s\"";

    private Runtime runtime;
    private File tempDirectory;

    public QuerySubmitter() {
        runtime = Runtime.getRuntime();
        tempDirectory = new File(System.getProperty("java.io.tmpdir"));
    }

    /**
     * Takes query specified in the query parameter file and submits it to Indri.
     * Stores the returned results in a file, returning the absolute path to that file.
     *
     * @param queryParamFile the absolute path of the
     * @return the absolute path to the file containing the query results
     * @throws IndriQueryException if querying Indri fails for some reasion.
     */
    public File submitQuery(File queryParamFile) {
        // first construct the command
        File resultFile = generateUniqueFile();
        String command = buildQueryCommand(queryParamFile, resultFile);

        // submit query to Indri. This will output the results in the result file.
        executeSubmit(command);

        // validate result
        validateResult(resultFile);

        return resultFile;
    }

    // generates file with unique name in the temp directory
    private File generateUniqueFile() {
        try {
            return File.createTempFile("sbr", null, tempDirectory);
        } catch(IOException exception) {
            throw new IndriQueryException("Could not generate unique file name", exception);
        }
    }

    // construct shell command for querying Indri
    private String buildQueryCommand(File queryParamFile, File resultFile) {
        return String.format(COMMAND_FORMAT, queryParamFile.getAbsolutePath(), resultFile.getAbsolutePath());
    }

    // execute shell command for querying Indri
    private void executeSubmit(String command) {
        try {
            runtime.exec(command);
        } catch (IOException e) {
            throw new IndriQueryException("Error submitting query", e);
        }
    }

    private void validateResult(File resultFile) {
        // TODO validate result of query. Throw some exception if something is wrong with the result
    }

}
