package com.ucl.search.sbr.domain.exception;

/**
 * Exception informing that something went wrong when generating a XML param file for
 * the Indri query.
 *
 * Created by Gabriel on 2/12/2015.
 */
public class GenerateParamXmlFileException extends RuntimeException {

    public GenerateParamXmlFileException(String message) {
        super(message);
    }

    public GenerateParamXmlFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenerateParamXmlFileException(Throwable cause) {
        super(cause);
    }
}
