package de.bail.classicmodels.util;

import javax.ws.rs.NotFoundException;

/**
 * Custom Not Found Error Exception
 * Will be accepted by resteasy and graphql
 */
public class CustomNotFoundException extends NotFoundException {

    public CustomNotFoundException() {
        super();
    }

    public CustomNotFoundException(String message) {
        super(message);
    }

}
