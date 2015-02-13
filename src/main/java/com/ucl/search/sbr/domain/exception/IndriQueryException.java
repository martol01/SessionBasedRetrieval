package com.ucl.search.sbr.domain.exception;

/**
 * Runtime exception to be raised when something goes wrong when querying Indri.
 *
 * Created by Gabriel on 2/12/2015.
 */
public class IndriQueryException extends RuntimeException {

    public IndriQueryException(String message) {
        super(message);
    }

    public IndriQueryException(Throwable cause) {
        super(cause);
    }

    public IndriQueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
