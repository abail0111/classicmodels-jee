package de.bail.classicmodels.resource.graphql.datafetcher.mutation;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bail.classicmodels.model.enities.Employee;
import de.bail.classicmodels.service.EmployeeService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.LinkedHashMap;

/**
 * Update Employee Data Fetcher
 */
public class UpdateEmployeeDataFetcher implements DataFetcher<Employee> {

    private final EmployeeService service;

    private final ObjectMapper mapper = new ObjectMapper();

    public UpdateEmployeeDataFetcher(EmployeeService service) {
        this.service = service;
    }

    @Override
    public Employee get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        LinkedHashMap<String, Object> employeeInput = dataFetchingEnvironment.getArgument("employee");
        // map employee object
        final Employee employee = mapper.convertValue(employeeInput, Employee.class);
        // create employee
        return service.update(employee);
    }

}
