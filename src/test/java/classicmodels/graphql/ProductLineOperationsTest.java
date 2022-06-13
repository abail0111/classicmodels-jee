package classicmodels.graphql;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.bail.classicmodels.model.enities.ProductLine;
import de.bail.classicmodels.resource.graphql.GraphQLServlet;
import de.bail.classicmodels.service.ProductLineService;
import de.bail.classicmodels.resource.provider.CustomGraphQLException;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasKey;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProductLineOperationsTest extends StaticGraphQLTest {

    @InjectMocks
    GraphQLServlet graphQLServlet;

    @Mock
    ProductLineService productLineService;

    ProductLine productLine;

    Gson gson = new Gson();

    @BeforeEach
    public void setup() {
        // instantiating productLine object
        productLine = new ProductLine();
        productLine.setId("Classic Cars");
        productLine.setTextDescription("Cars cars cars!");
        // mock productLine service
        when(productLineService.getEntityById(eq("1"))).thenReturn(productLine);
        when(productLineService.getEntityById(eq("2"))).thenThrow(new CustomGraphQLException(404, null));
        when(productLineService.getAllEntitiesPagination(anyInt(), anyInt())).thenReturn(Collections.singletonList(productLine));
        when(productLineService.getAllEntities()).thenReturn(Collections.singletonList(productLine));
        when(productLineService.count()).thenReturn(10);
        when(productLineService.create(ArgumentMatchers.any(ProductLine.class))).thenReturn(productLine);
        when(productLineService.update(argThat(new ProductLineMatcher("1")))).thenReturn(productLine);
        when(productLineService.update(argThat(new ProductLineMatcher("2")))).thenThrow(new CustomGraphQLException(404, null));
        when(productLineService.deleteById(eq("1"))).thenReturn(productLine);
        when(productLineService.deleteById(eq("2"))).thenThrow(new CustomGraphQLException(404, null));
    }

    // ------------ Test Queries ------------

    @Test
    public void testReadByID_Error_NotFound() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", "2");
        String responseJson = queryGraphQL("queries", "productLine", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("errors"), notNullValue());
        assertThat(jsonPath.get("errors[0].extensions.code"), equalTo(404));
    }

    @Test
    public void testReadByID() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", "1");
        String responseJson = queryGraphQL("queries", "productLine", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.productLine.id"), equalTo(productLine.getId()));
        assertThat(jsonPath.get("data.productLine.textDescription"), equalTo(productLine.getTextDescription()));
    }

    // ------------ Test Read All ------------

    @Test
    public void testReadAll() {
        JsonObject variables = new JsonObject();
        variables.addProperty("offset", 0);
        variables.addProperty("limit", 1);
        String responseJson = queryGraphQL("queries", "productLines", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.productLines[0].id"), equalTo(productLine.getId()));
        assertThat(jsonPath.get("data.productLines[0].textDescription"), equalTo(productLine.getTextDescription()));
    }

    // ------------ Test Create ------------
    
    @Test
    public void testCreate() {
        JsonObject variables = new JsonObject();
        variables.add("productLine", gson.toJsonTree(productLine));
        String responseJson = queryGraphQL("mutations", "createProductLine", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.createProductLine.id"), equalTo(productLine.getId()));
        assertThat(jsonPath.get("data.createProductLine.textDescription"), equalTo(productLine.getTextDescription()));
    }

    // ------------ Test Update ------------
    
    @Test
    public void testUpdate_Error_NotFound() {
        productLine.setId("2");
        JsonObject variables = new JsonObject();
        variables.add("productLine", gson.toJsonTree(productLine));
        String responseJson = queryGraphQL("mutations", "updateProductLine", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("errors"), notNullValue());
        assertThat(jsonPath.get("errors[0].extensions.code"), equalTo(404));
    }

    @Test
    public void testUpdate() {
        productLine.setId("1");
        JsonObject variables = new JsonObject();
        variables.add("productLine", gson.toJsonTree(productLine));
        String responseJson = queryGraphQL("mutations", "updateProductLine", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.updateProductLine.id"), equalTo(productLine.getId()));
        assertThat(jsonPath.get("data.updateProductLine.textDescription"), equalTo(productLine.getTextDescription()));
    }

    // ------------ Test Delete ------------

    @Test
    public void testDelete_Error_NotFound() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", "2");
        String responseJson = queryGraphQL("mutations", "deleteProductLine", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("errors"), notNullValue());
        assertThat(jsonPath.get("errors[0].extensions.code"), equalTo(404));
    }

    @Test
    public void testDelete() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", "1");
        String responseJson = queryGraphQL("mutations", "deleteProductLine", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.deleteProductLine.id"), equalTo(productLine.getId()));
        assertThat(jsonPath.get("data.deleteProductLine.textDescription"), equalTo(productLine.getTextDescription()));
    }

    /**
     * Employee Matcher
     */
    public static class ProductLineMatcher implements ArgumentMatcher<ProductLine> {

        private final String expectedId;

        public ProductLineMatcher(String id) {
            this.expectedId = id;
        }

        @Override
        public boolean matches(ProductLine productLine) {
            return productLine != null && productLine.getId().equals(expectedId);
        }
    }
}
