package de.bail.classicmodels.util;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ApplicationExceptionHandler implements ExceptionMapper<CustomGraphQLException> {

    @Override
    public Response toResponse(CustomGraphQLException exception) {
        switch (exception.getErrorCode()) {
            case 404 :
                return Response.status(Response.Status.NOT_FOUND).entity(exception.getMessage()).build();
            case 500 :
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage()).build();
            default:
                return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).build();
        }
    }

}
