package classicmodels.graphql;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.bail.classicmodels.model.enities.Customer;
import de.bail.classicmodels.model.enities.Employee;
import de.bail.classicmodels.resource.graphql.GraphQLServlet;
import de.bail.classicmodels.service.CustomerService;
import de.bail.classicmodels.service.OrderService;
import de.bail.classicmodels.service.PaymentService;
import de.bail.classicmodels.util.CustomGraphQLException;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasKey;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CustomerOperationsTest extends StaticGraphQLTest {

    @InjectMocks
    GraphQLServlet graphQLServlet;

    @Mock
    CustomerService customerService;

    @Mock
    PaymentService paymentService;

    @Mock
    OrderService orderService;

    Customer customer;

    Gson gson = new Gson();

    @BeforeEach
    public void setup() {
        // instantiating employee object
        Employee employee = new Employee();
        employee.setId(1);
        employee.setFirstName("Brendon");
        employee.setLastName("Storek");
        employee.setEmail("bendon.storek@classicmodels.com");
        employee.setExtension("x123");
        employee.setJobTitle("sales");
        // instantiating customer object
        customer = new Customer();
        customer.setId(1);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setCustomerName("Test Inc.");
        customer.setAddressLine1("6964 Farewell Avenue");
        customer.setSalesRepEmployee(employee);
        // mock customer service
        when(customerService.getEntityById(eq(1))).thenReturn(customer);
        when(customerService.getEntityById(eq(2))).thenThrow(new CustomGraphQLException(404, null));
        when(customerService.getAllEntitiesPagination(anyInt(), anyInt())).thenReturn(Collections.singletonList(customer));
        when(customerService.getAllEntities()).thenReturn(Collections.singletonList(customer));
        when(customerService.count()).thenReturn(10);
        when(customerService.create(any(Customer.class))).thenReturn(customer);
        when(customerService.update(argThat(new CustomerMatcher(1)))).thenReturn(customer);
        when(customerService.update(argThat(new CustomerMatcher(2)))).thenThrow(new CustomGraphQLException(404, null));
        when(customerService.deleteById(eq(1))).thenReturn(customer);
        when(customerService.deleteById(eq(2))).thenThrow(new CustomGraphQLException(404, null));
        // mock payment service
        when(paymentService.getAllByCustomer(anyList())).thenReturn((Collections.emptyList()));
        // mock order service
        when(orderService.getAllByCustomer(anyList())).thenReturn((Collections.emptyList()));
    }

    // ------------ Test Queries ------------

    @Test
    public void testReadByID() {
        // graphql query
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 1);
        String responseJson = queryGraphQL("queries", "customer", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.customer.id"), equalTo(customer.getId()));
        assertThat(jsonPath.get("data.customer.firstName"), equalTo(customer.getFirstName()));
        assertThat(jsonPath.get("data.customer.lastName"), equalTo(customer.getLastName()));
    }

    @Test
    public void testReadByID_Error_NotFound() {
        // graphql query
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 2);
        String responseJson = queryGraphQL("queries", "customer", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("errors"), notNullValue());
        assertThat(jsonPath.get("errors[0].extensions.code"), equalTo(404));
    }

    // ------------ Test Read All ------------

    @Test
    public void testReadAll() {
        JsonObject variables = new JsonObject();
        variables.addProperty("offset", 0);
        variables.addProperty("limit", 1);
        String responseJson = queryGraphQL("queries", "customers", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.customers[0].id"), equalTo(customer.getId()));
        assertThat(jsonPath.get("data.customers[0].firstName"), equalTo(customer.getFirstName()));
        assertThat(jsonPath.get("data.customers[0].lastName"), equalTo(customer.getLastName()));
    }

    // ------------ Test Create ------------

    @Test
    public void testCreate() {
        JsonObject variables = new JsonObject();
        variables.add("customer", gson.toJsonTree(customer));
        String responseJson = queryGraphQL("mutations", "createCustomer", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.createCustomer.id"), equalTo(customer.getId()));
        assertThat(jsonPath.get("data.createCustomer.firstName"), equalTo(customer.getFirstName()));
        assertThat(jsonPath.get("data.createCustomer.lastName"), equalTo(customer.getLastName()));
    }

    // ------------ Test Update ------------

    @Test
    public void testUpdate() {
        JsonObject variables = new JsonObject();
        variables.add("customer", gson.toJsonTree(customer));
        String responseJson = queryGraphQL("mutations", "updateCustomer", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.updateCustomer.id"), equalTo(customer.getId()));
        assertThat(jsonPath.get("data.updateCustomer.firstName"), equalTo(customer.getFirstName()));
        assertThat(jsonPath.get("data.updateCustomer.lastName"), equalTo(customer.getLastName()));
    }

    @Test
    public void testUpdate_Error_NotFound() {
        customer.setId(2);
        JsonObject variables = new JsonObject();
        variables.add("customer", gson.toJsonTree(customer));
        String responseJson = queryGraphQL("mutations", "updateCustomer", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("errors"), notNullValue());
        assertThat(jsonPath.get("errors[0].extensions.code"), equalTo(404));
    }

    // ------------ Test Delete ------------

    @Test
    public void testDelete() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 1);
        String responseJson = queryGraphQL("mutations", "deleteCustomer", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.deleteCustomer.id"), equalTo(customer.getId()));
        assertThat(jsonPath.get("data.deleteCustomer.firstName"), equalTo(customer.getFirstName()));
        assertThat(jsonPath.get("data.deleteCustomer.lastName"), equalTo(customer.getLastName()));
    }

    @Test
    public void testDelete_Error_NotFound() {
        // graphql query
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 2);
        String responseJson = queryGraphQL("mutations", "deleteCustomer", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("errors"), notNullValue());
        assertThat(jsonPath.get("errors[0].extensions.code"), equalTo(404));
    }

    /**
     * Customer Matcher
     */
    public static class CustomerMatcher implements ArgumentMatcher<Customer> {

        private final Integer expectedId;

        public CustomerMatcher(Integer id) {
            this.expectedId = id;
        }

        @Override
        public boolean matches(Customer customer) {
            return customer != null && customer.getId().equals(expectedId);
        }
    }
}
