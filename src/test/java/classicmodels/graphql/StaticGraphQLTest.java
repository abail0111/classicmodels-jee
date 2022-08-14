package classicmodels.graphql;

import com.google.gson.JsonObject;
import de.bail.classicmodels.resource.graphql.GraphQLServlet;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.UriBuilder;
import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;

/**
 * Execute static graphql queries using the default endpoint: /graphql.
 * Return Rest Assured Response
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class StaticGraphQLTest {

    /**
     * Execute static graphql queries from /test/resources/graphql/ with file extension '.gql'.
     * The default endpoint /graphql is used for all requests.
     * @param directory queries, mutations or subscription
     * @param query Query name: File name and query name should be named the same.
     * @param variables Json object with variables
     * @param graphQLServlet GraphQLServlet instance
     * @return Response body as string
     */
    public String queryGraphQL(String directory, String query, JsonObject variables, GraphQLServlet graphQLServlet) {
        // create payload
        JsonObject payloadJson = new JsonObject();
        payloadJson.addProperty("operationName", query);
        payloadJson.addProperty("query", loadDocument(directory, query));
        payloadJson.add("variables", variables);
        String payload = payloadJson.toString();
        // mock servlet request and response
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        // create output stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ServletOutputStream servletStream = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {

            }
        };
        try {
            // mock servlet request and response
            when(request.getMethod()).thenReturn("POST");
            when(request.getReader()).thenReturn(new BufferedReader(new StringReader(payload)));
            when(response.getOutputStream()).thenReturn(servletStream);
            // execute query
            graphQLServlet.service(request, response);
            // ensure that the response is setup as expected
            // verify(response).setContentType(MediaType.APPLICATION_JSON);
            // verify(response).setStatus(200);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
        return outputStream.toString(StandardCharsets.UTF_8);
    }

    /**
     * Load query document form /test/resources/graphql/
     * @param directory queries, mutations or subscription
     * @param documentName document name without file extension '.gql'
     * @return GraphQL Query or Mutation from resources
     */
    public String loadDocument(String directory, String documentName) {
        try {
            UriBuilder uriBuilder = UriBuilder.fromUri("/graphql/operations/");
            uriBuilder.path(directory)
                    .path(documentName + ".gql");
            InputStream inputStream = this.getClass().getResourceAsStream(uriBuilder.build().toString());
            if (inputStream == null) {
                throw new IllegalArgumentException("query not found: " + uriBuilder.build().toString());
            } else {
                return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //        try {
    //            ObjectMapper mapper = new ObjectMapper();
    //            HashMap<String, Object> vars = mapper.readValue(variables.toString(), HashMap.class);
    //            // create GraphQL request
    //            GraphQLRequest request = new GraphQLRequest(loadDocument(directory, query), vars, new HashMap<>(), query);
    //            // execute query
    //            return graphQLServlet.executeQuery(request.getQuery());
    //        } catch (JsonProcessingException e) {
    //            e.printStackTrace();
    //        }
    //        return null;
}
