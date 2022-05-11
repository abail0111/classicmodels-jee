package classicmodels.graphql;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.bail.classicmodels.model.enities.*;
import de.bail.classicmodels.resource.graphql.GraphQLServlet;
import de.bail.classicmodels.service.OrderDetailService;
import de.bail.classicmodels.service.OrderService;
import de.bail.classicmodels.util.CustomGraphQLException;
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

import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OrderOperationsTest extends StaticGraphQLTest {

    @InjectMocks
    GraphQLServlet graphQLServlet;

    @Mock
    OrderService orderService;

    @Mock
    OrderDetailService orderDetailService;

    Order order;

    Gson gson = new Gson();

    @BeforeEach
    public void setup() {
        // instantiating order detail object
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderNumber(1);
        orderDetail.setProduct(new Product());
        orderDetail.setPriceEach(19.99);
        // instantiating customer object
        Customer customer = new Customer();
        customer.setId(1);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setCustomerName("Test Inc.");
        customer.setAddressLine1("6964 Farewell Avenue");
        // instantiating order object
        order = new Order();
        order.setId(1);
        order.setStatus("Shipped");
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.of(2022,3,20,0,0, 1));
        // mock order service
        when(orderService.getEntityById(eq(1))).thenReturn(order);
        when(orderService.getEntityById(eq(2))).thenThrow(new CustomGraphQLException(404, null));
        when(orderService.getAllEntitiesPagination(anyInt(), anyInt())).thenReturn(Collections.singletonList(order));
        when(orderService.getAllEntities()).thenReturn(Collections.singletonList(order));
        when(orderService.count()).thenReturn(10);
        when(orderService.create(ArgumentMatchers.any(Order.class))).thenReturn(order);
        when(orderService.update(argThat(new OrderMatcher(1)))).thenReturn(order);
        when(orderService.update(argThat(new OrderMatcher(2)))).thenThrow(new CustomGraphQLException(404, null));
        when(orderService.deleteById(eq(1))).thenReturn(order);
        doThrow(new CustomGraphQLException(404, null)).when(orderService).deleteById(eq(2));
        // mock order detail service
        when(orderDetailService.getAllByOrder(anyInt(), anyInt(), anyInt())).thenReturn(Collections.singletonList(orderDetail));
    }

    // ------------ Test Queries ------------
    
    @Test
    public void testReadByID_Error_NotFound() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 2);
        String responseJson = queryGraphQL("queries", "order", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("errors"), notNullValue());
        assertThat(jsonPath.get("errors[0].extensions.code"), equalTo(404));
    }

    @Test
    public void testReadByID() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 1);
        String responseJson = queryGraphQL("queries", "order", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.order.id"), equalTo(order.getId()));
        assertThat(jsonPath.get("data.order.status"), equalTo(order.getStatus()));
        assertThat(jsonPath.get("data.order.orderDate"), equalTo(order.getOrderDate().toString()));
    }

    // ------------ Test Read All ------------
    
    @Test
    public void testReadAll() {
        JsonObject variables = new JsonObject();
        variables.addProperty("offset", 0);
        variables.addProperty("limit", 1);
        String responseJson = queryGraphQL("queries", "orders", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.orders[0].id"), equalTo(order.getId()));
        assertThat(jsonPath.get("data.orders[0].status"), equalTo(order.getStatus()));
        assertThat(jsonPath.get("data.orders[0].orderDate"), equalTo(order.getOrderDate().toString()));
    }

    // ------------ Test Create ------------

    @Test
    public void testCreate() {
        JsonObject orderJson = gson.toJsonTree(order).getAsJsonObject();
        orderJson.remove("orderDate");
        orderJson.addProperty("orderDate", order.getOrderDate().toString());
        JsonObject variables = new JsonObject();
        variables.add("order", orderJson);
        String responseJson = queryGraphQL("mutations", "createOrder", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.createOrder.id"), equalTo(order.getId()));
        assertThat(jsonPath.get("data.createOrder.status"), equalTo(order.getStatus()));
        assertThat(jsonPath.get("data.createOrder.orderDate"), equalTo(order.getOrderDate().toString()));
    }

    // ------------ Test Update ------------

    @Test
    public void testUpdate_Error_NotFound() {
        order.setId(2);
        JsonObject orderJson = gson.toJsonTree(order).getAsJsonObject();
        orderJson.remove("orderDate");
        orderJson.addProperty("orderDate", order.getOrderDate().toString());
        JsonObject variables = new JsonObject();
        variables.add("order", orderJson);
        String responseJson = queryGraphQL("mutations", "updateOrder", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("errors"), notNullValue());
        assertThat(jsonPath.get("errors[0].extensions.code"), equalTo(404));
    }

    @Test
    public void testUpdate() {
        JsonObject orderJson = gson.toJsonTree(order).getAsJsonObject();
        orderJson.remove("orderDate");
        orderJson.addProperty("orderDate", order.getOrderDate().toString());
        JsonObject variables = new JsonObject();
        variables.add("order", orderJson);
        String responseJson = queryGraphQL("mutations", "updateOrder", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.updateOrder.id"), equalTo(order.getId()));
        assertThat(jsonPath.get("data.updateOrder.status"), equalTo(order.getStatus()));
        assertThat(jsonPath.get("data.updateOrder.orderDate"), equalTo(order.getOrderDate().toString()));
    }

    // ------------ Test Delete ------------

    @Test
    public void testDelete_Error_NotFound() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 2);
        String responseJson = queryGraphQL("mutations", "deleteOrder", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("errors"), notNullValue());
        assertThat(jsonPath.get("errors[0].extensions.code"), equalTo(404));
    }

    @Test
    public void testDelete() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 1);
        String responseJson = queryGraphQL("mutations", "deleteOrder", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.deleteOrder.id"), equalTo(order.getId()));
        assertThat(jsonPath.get("data.deleteOrder.status"), equalTo(order.getStatus()));
        assertThat(jsonPath.get("data.deleteOrder.orderDate"), equalTo(order.getOrderDate().toString()));
    }

    /**
     * Order Matcher
     */
    public static class OrderMatcher implements ArgumentMatcher<Order> {

        private final Integer expectedId;

        public OrderMatcher(Integer id) {
            this.expectedId = id;
        }

        @Override
        public boolean matches(Order order) {
            return order != null && order.getId().equals(expectedId);
        }
    }
}
