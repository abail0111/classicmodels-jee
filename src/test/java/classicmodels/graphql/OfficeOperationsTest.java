package classicmodels.graphql;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.bail.classicmodels.model.enities.Employee;
import de.bail.classicmodels.model.enities.Office;
import de.bail.classicmodels.resource.graphql.GraphQLServlet;
import de.bail.classicmodels.service.EmployeeService;
import de.bail.classicmodels.service.OfficeService;
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
public class OfficeOperationsTest extends StaticGraphQLTest {

    @InjectMocks
    GraphQLServlet graphQLServlet;

    @Mock
    OfficeService officeService;

    @Mock
    EmployeeService employeeService;

    Office office;

    Gson gson = new Gson();

    @BeforeEach
    public void setup() {
        // instantiating office object
        office = new Office();
        office.setId(1);
        office.setCity("Berlin");
        office.setPhone("+49 12345 112233");
        office.setAddressLine1("997 Classic Street");
        office.setCountry("Germany");
        office.setPostalCode("10115");
        office.setTerritory("EMEA");
        // instantiating office object
        Employee employee = new Employee();
        employee.setId(1);
        employee.setFirstName("Brendon");
        employee.setLastName("Storek");
        employee.setEmail("bendon.storek@classicmodels.com");
        employee.setExtension("x123");
        employee.setJobTitle("sales");
        employee.setOffice(office);
        employee.setReportsTo(employee);
        // mock office service
        when(officeService.getEntityById(eq(1))).thenReturn(office);
        when(officeService.getEntityById(eq(2))).thenThrow(new CustomGraphQLException(404, null));
        when(officeService.getAllEntitiesPagination(anyInt(), anyInt())).thenReturn(Collections.singletonList(office));
        when(officeService.getAllEntities()).thenReturn(Collections.singletonList(office));
        when(officeService.count()).thenReturn(10);
        when(officeService.create(ArgumentMatchers.any(Office.class))).thenReturn(office);
        when(officeService.update(argThat(new OfficeMatcher(1)))).thenReturn(office);
        when(officeService.update(argThat(new OfficeMatcher(2)))).thenThrow(new CustomGraphQLException(404, null));
        when(officeService.deleteById(eq(1))).thenReturn(office);
        when(officeService.deleteById(eq(2))).thenThrow(new CustomGraphQLException(404, null));
        // mock employee service
        when(employeeService.getAllByOffice((anyList()))).thenReturn(Collections.singletonList(employee));
    }

    // ------------ Test Queries ------------

    @Test
    public void testReadByID() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", "1");
        String responseJson = queryGraphQL("queries", "office", variables, graphQLServlet);
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.office.id"), equalTo(office.getId()));
        assertThat(jsonPath.get("data.office.city"), equalTo(office.getCity()));
        assertThat(jsonPath.get("data.office.country"), equalTo(office.getCountry()));
    }

    @Test
    public void testReadByID_Error_NotFound() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", "2");
        String responseJson = queryGraphQL("queries", "office", variables, graphQLServlet);
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
        String responseJson = queryGraphQL("queries", "offices", variables, graphQLServlet);
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.offices[0].id"), equalTo(office.getId()));
        assertThat(jsonPath.get("data.offices[0].city"), equalTo(office.getCity()));
        assertThat(jsonPath.get("data.offices[0].country"), equalTo(office.getCountry()));
    }

    // ------------ Test Create ------------

    @Test
    public void testCreate() {
        JsonObject variables = new JsonObject();
        variables.add("office", gson.toJsonTree(office));
        String responseJson = queryGraphQL("mutations", "createOffice", variables, graphQLServlet);
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.createOffice.id"), equalTo(office.getId()));
        assertThat(jsonPath.get("data.createOffice.city"), equalTo(office.getCity()));
        assertThat(jsonPath.get("data.createOffice.country"), equalTo(office.getCountry()));
    }

    // ------------ Test Update ------------

    @Test
    public void testUpdate_Error_NotFound() {
        office.setId(2);
        JsonObject variables = new JsonObject();
        variables.add("office", gson.toJsonTree(office));
        String responseJson = queryGraphQL("mutations", "updateOffice", variables, graphQLServlet);
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("errors"), notNullValue());
        assertThat(jsonPath.get("errors[0].extensions.code"), equalTo(404));
    }

    @Test
    public void testUpdate_DataObject() {
        JsonObject variables = new JsonObject();
        variables.add("office", gson.toJsonTree(office));
        String responseJson = queryGraphQL("mutations", "updateOffice", variables, graphQLServlet);
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.updateOffice.id"), equalTo(office.getId()));
        assertThat(jsonPath.get("data.updateOffice.city"), equalTo(office.getCity()));
        assertThat(jsonPath.get("data.updateOffice.country"), equalTo(office.getCountry()));
    }

    // ------------ Test Delete ------------

    @Test
    public void testDelete_Error_NotFound() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", "2");
        String responseJson = queryGraphQL("mutations", "deleteOffice", variables, graphQLServlet);
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("errors"), notNullValue());
        assertThat(jsonPath.get("errors[0].extensions.code"), equalTo(404));
    }

    @Test
    public void testDelete() {
        JsonObject variables = new JsonObject();
        variables.addProperty("id", "1");
        String responseJson = queryGraphQL("mutations", "deleteOffice", variables, graphQLServlet);
        JsonPath jsonPath = new JsonPath(responseJson);
        assertThat(jsonPath.get("$"), not(hasKey("errors")));
        assertThat(jsonPath.get("data"), notNullValue());
        assertThat(jsonPath.get("data.deleteOffice.id"), equalTo(office.getId()));
        assertThat(jsonPath.get("data.deleteOffice.city"), equalTo(office.getCity()));
        assertThat(jsonPath.get("data.deleteOffice.country"), equalTo(office.getCountry()));
    }

    /**
     * Employee Matcher
     */
    public static class OfficeMatcher implements ArgumentMatcher<Office> {

        private final Integer expectedId;

        public OfficeMatcher(Integer id) {
            this.expectedId = id;
        }

        @Override
        public boolean matches(Office office) {
            return office != null && office.getId().equals(expectedId);
        }
    }
}
