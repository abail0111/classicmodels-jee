package classicmodels.graphql;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.bail.classicmodels.model.enities.Product;
import de.bail.classicmodels.model.enities.ProductLine;
import de.bail.classicmodels.resource.graphql.GraphQLServlet;
import de.bail.classicmodels.service.ProductService;
import de.bail.classicmodels.resource.provider.CustomGraphQLException;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

@Disabled
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProductOperationsTest extends StaticGraphQLTest {
    
    @InjectMocks
    GraphQLServlet graphQLServlet;

    @Mock
    ProductService productService;

    Product product;

    Gson gson = new Gson();

    @BeforeEach
    public void setup() {
        // instantiating product line object
        ProductLine productLine = new ProductLine();
        productLine.setId("Classic Cars");
        productLine.setTextDescription("Cars cars cars!");
        // instantiating product object
        product = new Product();
        product.setId("1");
        product.setProductName("Porsche 911");
        product.setProductLine(productLine);
        product.setProductScale("1:18");
        product.setProductVendor("Porsche Models");
        product.setQuantityInStock((short) 999);
        product.setBuyPrice(19.99);
        product.setMsrp(45.15);
        // mock product service
        when(productService.getEntityById(eq("1"))).thenReturn(product);
        when(productService.getEntityById(eq("2"))).thenThrow(new CustomGraphQLException(404, null));
        when(productService.getAllEntitiesPagination(anyInt(), anyInt())).thenReturn(Collections.singletonList(product));
        when(productService.getAllEntities()).thenReturn(Collections.singletonList(product));
        when(productService.count()).thenReturn(10);
        when(productService.create(ArgumentMatchers.any(Product.class))).thenReturn(product);
        when(productService.update(argThat(new ProductMatcher("1")))).thenReturn(product);
        when(productService.update(argThat(new ProductMatcher("2")))).thenThrow(new CustomGraphQLException(404, null));
        when(productService.deleteById(eq("1"))).thenReturn(product);
        when(productService.deleteById(eq("2"))).thenThrow(new CustomGraphQLException(404, null));
    }

    // ------------ Test Queries ------------

    @Test
    public void testReadByID_Error_NotFound() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", "2");
        String responseJson = queryGraphQL("queries", "product", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("errors"), notNullValue());
        assertThat(jsonPath.get("errors[0].extensions.code"), equalTo(404));
    }

    @Test
    public void testReadByID() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", "1");
        String responseJson = queryGraphQL("queries", "product", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.product.id"), equalTo(product.getId()));
        assertThat(jsonPath.get("data.product.productName"), equalTo(product.getProductName()));
        assertThat(jsonPath.get("data.product.productLine.id"), equalTo(product.getProductLine().getId()));
    }

    // ------------ Test Read All ------------

    @Test
    public void testReadAll() {
        JsonObject variables = new JsonObject();
        variables.addProperty("offset", 0);
        variables.addProperty("limit", 1);
        String responseJson = queryGraphQL("queries", "products", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.products[0].id"), equalTo(product.getId()));
        assertThat(jsonPath.get("data.products[0].productName"), equalTo(product.getProductName()));
        assertThat(jsonPath.get("data.products[0].productLine.id"), equalTo(product.getProductLine().getId()));
    }

    // ------------ Test Create ------------

    @Test
    public void testCreate() {
        JsonObject variables = new JsonObject();
        variables.add("product", gson.toJsonTree(product));
        String responseJson = queryGraphQL("mutations", "createProduct", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.createProduct.id"), equalTo(product.getId()));
        assertThat(jsonPath.get("data.createProduct.productName"), equalTo(product.getProductName()));
        assertThat(jsonPath.get("data.createProduct.productLine.id"), equalTo(product.getProductLine().getId()));
    }

    // ------------ Test Update ------------

    @Test
    public void testUpdate_Error_NotFound() {
        product.setId("2");
        JsonObject variables = new JsonObject();
        variables.add("product", gson.toJsonTree(product));
        String responseJson = queryGraphQL("mutations", "updateProduct", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("errors"), notNullValue());
        assertThat(jsonPath.get("errors[0].extensions.code"), equalTo(404));
    }

    @Test
    public void testUpdate() {
        JsonObject variables = new JsonObject();
        variables.add("product", gson.toJsonTree(product));
        String responseJson = queryGraphQL("mutations", "updateProduct", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.updateProduct.id"), equalTo(product.getId()));
        assertThat(jsonPath.get("data.updateProduct.productName"), equalTo(product.getProductName()));
        assertThat(jsonPath.get("data.updateProduct.productLine.id"), equalTo(product.getProductLine().getId()));
    }

    // ------------ Test Delete ------------

    @Test
    public void testDelete_Error_NotFound() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", "2");
        String responseJson = queryGraphQL("mutations", "deleteProduct", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("errors"), notNullValue());
        assertThat(jsonPath.get("errors[0].extensions.code"), equalTo(404));
    }

    @Test
    public void testDelete() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", "1");
        String responseJson = queryGraphQL("mutations", "deleteProduct", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.deleteProduct.id"), equalTo(product.getId()));
        assertThat(jsonPath.get("data.deleteProduct.productName"), equalTo(product.getProductName()));
        assertThat(jsonPath.get("data.deleteProduct.productLine.id"), equalTo(product.getProductLine().getId()));
    }

    /**
     * Employee Matcher
     */
    public static class ProductMatcher implements ArgumentMatcher<Product> {

        private final String expectedId;

        public ProductMatcher(String id) {
            this.expectedId = id;
        }

        @Override
        public boolean matches(Product product) {
            return product != null && product.getId().equals(expectedId);
        }
    }
}
