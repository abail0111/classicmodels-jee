package classicmodels.graphql;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.bail.classicmodels.model.enities.Customer;
import de.bail.classicmodels.model.enities.Employee;
import de.bail.classicmodels.model.enities.Office;
import de.bail.classicmodels.resource.graphql.GraphQLServlet;
import de.bail.classicmodels.service.CustomerService;
import de.bail.classicmodels.service.EmployeeService;

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

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Disabled
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EmployeeOperationsTest extends StaticGraphQLTest {

    @InjectMocks
    GraphQLServlet graphQLServlet;

    @Mock
    EmployeeService employeeService;

    @Mock
    CustomerService customerService;

    Employee employee;

    Gson gson = new Gson();

    @BeforeEach
    public void setup() {
        // instantiating office object
        Office office = new Office();
        office.setId(1);
        office.setCity("Berlin");
        office.setPhone("+49 12345 112233");
        office.setAddressLine1("997 Classic Street");
        office.setCountry("Germany");
        office.setPostalCode("10115");
        office.setTerritory("EMEA");
        // instantiating employee object
        employee = new Employee();
        employee.setId(1);
        employee.setFirstName("Brendon");
        employee.setLastName("Storek");
        employee.setEmail("bendon.storek@classicmodels.com");
        employee.setExtension("x123");
        employee.setJobTitle("sales");
        employee.setOffice(office);
        employee.setReportsTo(employee);
        // instantiating customer object
        Customer customer = new Customer();
        customer.setId(1);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setCustomerName("Test Inc.");
        customer.setAddressLine1("6964 Farewell Avenue");
        customer.setSalesRepEmployee(employee);
        // mock employee service
        when(employeeService.getEntityById(eq(1))).thenReturn(employee);
        when(employeeService.getEntityById(eq(2))).thenThrow(new CustomGraphQLException(404, null));
        when(employeeService.getAllEntitiesPagination(anyInt(), anyInt())).thenReturn(Collections.singletonList(employee));
        when(employeeService.getAllEntities()).thenReturn(Collections.singletonList(employee));
        when(employeeService.count()).thenReturn(10);
        when(employeeService.create(ArgumentMatchers.any(Employee.class))).thenReturn(employee);
        when(employeeService.update(argThat(new EmployeeMatcher(1)))).thenReturn(employee);
        when(employeeService.update(argThat(new EmployeeMatcher(2)))).thenThrow(new CustomGraphQLException(404, null));
        when(employeeService.deleteById(eq(1))).thenReturn(employee);
        doThrow(new CustomGraphQLException(404, null)).when(employeeService).deleteById(eq(2));
        // mock customer service
        when(customerService.getAllCustomerByEmployees((anyList()))).thenReturn(Collections.singletonList(customer));
    }

    // ------------ Test Queries ------------

    @Test
    public void testReadByID() {
        // graphql query
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 1);
        String responseJson = queryGraphQL("queries", "employee", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.employee.id"), equalTo(employee.getId()));
        assertThat(jsonPath.get("data.employee.firstName"), equalTo(employee.getFirstName()));
        assertThat(jsonPath.get("data.employee.lastName"), equalTo(employee.getLastName()));
    }

    @Test
    public void testReadByID_Error_NotFound() {
        // graphql query
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 2);
        String responseJson = queryGraphQL("queries", "employee", variables, graphQLServlet);
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
        String responseJson = queryGraphQL("queries", "employees", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.employees[0].id"), equalTo(employee.getId()));
        assertThat(jsonPath.get("data.employees[0].firstName"), equalTo(employee.getFirstName()));
        assertThat(jsonPath.get("data.employees[0].lastName"), equalTo(employee.getLastName()));
    }

    // ------------ Test Create ------------

    @Test
    public void testCreate() {
        JsonObject variables = new JsonObject();
        variables.add("employee", gson.toJsonTree(employee));
        String responseJson = queryGraphQL("mutations", "createEmployee", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.createEmployee.id"), equalTo(employee.getId()));
        assertThat(jsonPath.get("data.createEmployee.firstName"), equalTo(employee.getFirstName()));
        assertThat(jsonPath.get("data.createEmployee.lastName"), equalTo(employee.getLastName()));
    }

    // ------------ Test Update ------------

    @Test
    public void testUpdate() {
        JsonObject variables = new JsonObject();
        variables.add("employee", gson.toJsonTree(employee));
        String responseJson = queryGraphQL("mutations", "updateEmployee", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.updateEmployee.id"), equalTo(employee.getId()));
        assertThat(jsonPath.get("data.updateEmployee.firstName"), equalTo(employee.getFirstName()));
        assertThat(jsonPath.get("data.updateEmployee.lastName"), equalTo(employee.getLastName()));
    }

    @Test
    public void testUpdate_Error_NotFound() {
        employee.setId(2);
        JsonObject variables = new JsonObject();
        variables.add("employee", gson.toJsonTree(employee));
        String responseJson = queryGraphQL("mutations", "updateEmployee", variables, graphQLServlet);
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
        String responseJson = queryGraphQL("mutations", "deleteEmployee", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.deleteEmployee.id"), equalTo(employee.getId()));
        assertThat(jsonPath.get("data.deleteEmployee.firstName"), equalTo(employee.getFirstName()));
        assertThat(jsonPath.get("data.deleteEmployee.lastName"), equalTo(employee.getLastName()));
    }

    @Test
    public void testDelete_Error_NotFound() {
        // graphql query
        JsonObject variables = new JsonObject();
        variables.addProperty("id", 2);
        String responseJson = queryGraphQL("mutations", "deleteEmployee", variables, graphQLServlet);
        // validate response
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("errors"), notNullValue());
        assertThat(jsonPath.get("errors[0].extensions.code"), equalTo(404));
    }

    /**
     * Employee Matcher
     */
    public static class EmployeeMatcher implements ArgumentMatcher<Employee> {

        private final Integer expectedId;

        public EmployeeMatcher(Integer id) {
            this.expectedId = id;
        }

        @Override
        public boolean matches(Employee employee) {
            return employee != null && employee.getId().equals(expectedId);
        }
    }
}
