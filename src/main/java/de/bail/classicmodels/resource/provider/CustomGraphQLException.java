package de.bail.classicmodels.resource.provider;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom GraphQL Exception
 */
public class CustomGraphQLException extends RuntimeException implements GraphQLError {

    private final int errorCode;

    public CustomGraphQLException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public Map<String, Object> getExtensions() {
        Map<String, Object> customAttributes = new LinkedHashMap<>();
        customAttributes.put("code", this.errorCode);
        customAttributes.put("errorMessage", this.getMessage());
        return customAttributes;
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorType getErrorType() {
        return null;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
