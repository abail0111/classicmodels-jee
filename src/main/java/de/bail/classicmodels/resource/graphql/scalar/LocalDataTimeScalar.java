package de.bail.classicmodels.resource.graphql.scalar;

import graphql.language.StringValue;
import graphql.schema.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class LocalDataTimeScalar {

    public static final GraphQLScalarType SCALAR_TYPE = GraphQLScalarType.newScalar()
            .name("localDateTime")
            .description("A custom scalar that handles LocalDateTime")
            .coercing(new Coercing() {
                @Override
                public Object serialize(Object dataFetcherResult) {
                    return serializeLocalDateTime(dataFetcherResult);
                }

                @Override
                public Object parseValue(Object input) {
                    return parseLocalDateTimeFromVariable(input);
                }

                @Override
                public Object parseLiteral(Object input) {
                    return parseLocalDateTimeFromAstLiteral(input);
                }
            })
            .build();

    private static Object serializeLocalDateTime(Object dataFetcherResult) {
        if (dataFetcherResult instanceof LocalDateTime) {
            return ((LocalDateTime) dataFetcherResult).toString();
        } else {
            throw new CoercingSerializeException("Unable to serialize " + dataFetcherResult + " as LocalDateTime");
        }
    }

    private static Object parseLocalDateTimeFromVariable(Object input) {
        if (input instanceof String) {
            try {
                return LocalDateTime.parse(input.toString());
            } catch (DateTimeParseException e) {
                throw new CoercingParseValueException("Unable to parse variable value " + input + " as LocalDateTime");
            }
        }
        throw new CoercingParseValueException("Unable to parse variable value " + input + " as LocalDateTime");
    }

    private static Object parseLocalDateTimeFromAstLiteral(Object input) {
        if (input instanceof StringValue) {
            try {
                return LocalDateTime.parse(((StringValue) input).getValue());
            } catch (DateTimeParseException e) {
                throw new CoercingParseLiteralException("Value is not any localDateTime address : '" + String.valueOf(input) + "'");
            }
        }
        throw new CoercingParseLiteralException("Value is not any localDateTime address : '" + String.valueOf(input) + "'");
    }
}
