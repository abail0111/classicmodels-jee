package de.bail.classicmodels.resource.graphql.datafetcher.mutation;

import de.bail.classicmodels.model.enities.Employee;
import de.bail.classicmodels.service.EmployeeService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

/**
 * Delete Employee Data Fetcher
 */
public class DeleteEmployeeDataFetcher implements DataFetcher<Employee> {

    private final EmployeeService service;

    public DeleteEmployeeDataFetcher(EmployeeService service) {
        this.service = service;
    }

    @Override
    public Employee get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        int id = dataFetchingEnvironment.getArgument("id");
        return service.deleteById(id);
    }

}
