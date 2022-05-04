package de.bail.classicmodels.resource.graphql.datafetcher;

import de.bail.classicmodels.model.enities.Employee;
import de.bail.classicmodels.service.EmployeeService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;

/**
 * Employees Data Fetcher
 */
public class EmployeesDataFetcher implements DataFetcher<List<Employee>> {

    private final EmployeeService service;

    public EmployeesDataFetcher(EmployeeService service) {
        this.service = service;
    }

    @Override
    public List<Employee> get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        int limit = dataFetchingEnvironment.getArgument("limit");
        int offset = dataFetchingEnvironment.getArgument("offset");
        return service.getAllEntitiesPagination(offset, limit);
    }

}
