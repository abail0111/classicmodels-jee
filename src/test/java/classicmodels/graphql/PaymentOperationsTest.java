package classicmodels.graphql;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.bail.classicmodels.model.enities.Payment;
import de.bail.classicmodels.resource.graphql.GraphQLServlet;
import de.bail.classicmodels.service.PaymentService;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasKey;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PaymentOperationsTest extends StaticGraphQLTest {

    @InjectMocks
    GraphQLServlet graphQLServlet;

    @Mock
    PaymentService paymentService;

    Payment payment;

    Gson gson = new Gson();

    @BeforeEach
    public void setup() {
        // instantiating payment object
        payment = new Payment();
        payment.setCustomerNumber(1);
        payment.setCheckNumber("1");
        payment.setPaymentDate(Timestamp.valueOf(LocalDateTime.of(2022, 3,20, 0, 0, 0)));
        payment.setAmount(19.99);
        // mock payment service
        when(paymentService.getEntityById(eq(new Payment.PaymentId(1,"1")))).thenReturn(payment);
        when(paymentService.getEntityById(eq(new Payment.PaymentId(2,"2")))).thenThrow(new CustomGraphQLException(404, null));
        when(paymentService.getAllEntitiesPagination(anyInt(), anyInt())).thenReturn(Collections.singletonList(payment));
        when(paymentService.getAllEntities()).thenReturn(Collections.singletonList(payment));
        when(paymentService.count()).thenReturn(10);
        when(paymentService.create(ArgumentMatchers.any(Payment.class))).thenReturn(payment);
        when(paymentService.update(argThat(new PaymentMatcher(1,"1")))).thenReturn(payment);
        when(paymentService.update(argThat(new PaymentMatcher(2,"2")))).thenThrow(new CustomGraphQLException(404, null));
        when(paymentService.deleteById(eq(new Payment.PaymentId(1,"1")))).thenReturn(payment);
        when(paymentService.deleteById(eq(new Payment.PaymentId(2,"2")))).thenThrow(new CustomGraphQLException(404, null));
    }

    // ------------ Test Queries ------------

    @Test
    public void testReadByID_Error_NotFound() {
        JsonObject variables = new JsonObject();
        variables.addProperty("checkNumber", "2");
        variables.addProperty("customerNumber", 2);
        String responseJson = queryGraphQL("queries", "payment", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("errors"), notNullValue());
        assertThat(jsonPath.get("errors[0].extensions.code"), equalTo(404));
    }

    @Test
    public void testReadByID() {
        JsonObject variables = new JsonObject();
        variables.addProperty("checkNumber", "1");
        variables.addProperty("customerNumber", 1);
        String responseJson = queryGraphQL("queries", "payment", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.payment.id.checkNumber"), equalTo(payment.getCheckNumber()));
        assertThat(jsonPath.get("data.payment.id.customerNumber"), equalTo(payment.getCustomerNumber()));
    }

    // ------------ Test Read All ------------

    @Test
    public void testReadAll() {
        JsonObject variables = new JsonObject();
        variables.addProperty("offset", 0);
        variables.addProperty("limit", 1);
        String responseJson = queryGraphQL("queries", "payments", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.payments[0].id.checkNumber"), equalTo(payment.getCheckNumber()));
        assertThat(jsonPath.get("data.payments[0].id.customerNumber"), equalTo(payment.getCustomerNumber()));
    }

    // ------------ Test Create ------------

    @Test
    public void testCreate() {
        // prepare payment input object
        JsonObject paymentJson = gson.toJsonTree(payment).getAsJsonObject();
        paymentJson.remove("paymentDate");
        paymentJson.remove("checkNumber");
        paymentJson.remove("customerNumber");
        paymentJson.add("id", gson.toJsonTree(payment.getId()));
        paymentJson.addProperty("paymentDate", payment.getPaymentDate().toLocalDateTime().toString());
        // define variables
        JsonObject variables = new JsonObject();
        variables.add("payment", paymentJson);
        String responseJson = queryGraphQL("mutations", "createPayment", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.createPayment.id.checkNumber"), equalTo(payment.getCheckNumber()));
        assertThat(jsonPath.get("data.createPayment.id.customerNumber"), equalTo(payment.getCustomerNumber()));
    }

    // ------------ Test Update ------------

    @Test
    public void testUpdate_Error_NotFound() {
        // prepare payment input object
        JsonObject paymentJson = gson.toJsonTree(payment).getAsJsonObject();
        paymentJson.remove("paymentDate");
        paymentJson.remove("checkNumber");
        paymentJson.remove("customerNumber");
        paymentJson.add("id", gson.toJsonTree(new Payment.PaymentId(2,"2")));
        paymentJson.addProperty("paymentDate", payment.getPaymentDate().toLocalDateTime().toString());
        // define variables
        JsonObject variables = new JsonObject();
        variables.add("payment", paymentJson);
        String responseJson = queryGraphQL("mutations", "updatePayment", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("errors"), notNullValue());
        assertThat(jsonPath.get("errors[0].extensions.code"), equalTo(404));
    }

    @Test
    public void testUpdate() {
        // prepare payment input object
        JsonObject paymentJson = gson.toJsonTree(payment).getAsJsonObject();
        paymentJson.remove("paymentDate");
        paymentJson.remove("checkNumber");
        paymentJson.remove("customerNumber");
        paymentJson.add("id", gson.toJsonTree(payment.getId()));
        paymentJson.addProperty("paymentDate", payment.getPaymentDate().toLocalDateTime().toString());
        // define variables
        JsonObject variables = new JsonObject();
        variables.add("payment", paymentJson);
        String responseJson = queryGraphQL("mutations", "updatePayment", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.updatePayment.id.checkNumber"), equalTo(payment.getCheckNumber()));
        assertThat(jsonPath.get("data.updatePayment.id.customerNumber"), equalTo(payment.getCustomerNumber()));
    }

    // ------------ Test Delete ------------

    @Test
    public void testDelete_Error_NotFound() {
        JsonObject variables = new JsonObject();
        variables.addProperty("checkNumber", "2");
        variables.addProperty("customerNumber", 2);
        String responseJson = queryGraphQL("mutations", "deletePayment", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("errors"), notNullValue());
        assertThat(jsonPath.get("errors[0].extensions.code"), equalTo(404));
    }

    @Test
    public void testDelete() {
        JsonObject variables = new JsonObject();
        variables.addProperty("checkNumber", "1");
        variables.addProperty("customerNumber", 1);
        String responseJson = queryGraphQL("mutations", "deletePayment", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.deletePayment.id.checkNumber"), equalTo(payment.getCheckNumber()));
        assertThat(jsonPath.get("data.deletePayment.id.customerNumber"), equalTo(payment.getCustomerNumber()));
    }

    /**
     * Payment Matcher
     */
    public static class PaymentMatcher implements ArgumentMatcher<Payment> {

        private final Integer expectedCustomerNumber;
        private final String expectedCheckNumber;

        public PaymentMatcher(Integer customerNumber, String checkNumber) {
            this.expectedCustomerNumber = customerNumber;
            this.expectedCheckNumber = checkNumber;
        }

        @Override
        public boolean matches(Payment payment) {
            return payment != null
                    && payment.getId().getCustomerNumber().equals(expectedCustomerNumber)
                    && payment.getId().getCheckNumber().equals(expectedCheckNumber);
        }
    }
}
