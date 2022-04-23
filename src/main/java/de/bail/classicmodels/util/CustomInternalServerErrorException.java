package de.bail.classicmodels.util;

import javax.ws.rs.InternalServerErrorException;

/**
 * Custom Internal Server Error Exception
 * Will be accepted by resteasy and graphql
 */
public class CustomInternalServerErrorException extends InternalServerErrorException {

    public CustomInternalServerErrorException() {
        super();
    }

    public CustomInternalServerErrorException(String message) {
        super(message);
    }

}
