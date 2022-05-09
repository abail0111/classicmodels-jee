package de.bail.classicmodels.resource.graphql.datafetcher.query;

import de.bail.classicmodels.model.enities.Employee;
import de.bail.classicmodels.service.EmployeeService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

/**
 * Employee Data Fetcher
 */
public class EmployeeDataFetcher implements DataFetcher<Employee> {

    private final EmployeeService service;

    public EmployeeDataFetcher(EmployeeService service) {
        this.service = service;
    }

    @Override
    public Employee get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        int id = dataFetchingEnvironment.getArgument("id");
        return service.getEntityById(id);
    }

}
