package de.bail.classicmodels.resource.graphql.datafetcher.query;

import de.bail.classicmodels.model.enities.Customer;
import de.bail.classicmodels.service.CustomerService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;

/**
 * Customers Data Fetcher
 */
public class CustomersDataFetcher implements DataFetcher<List<Customer>> {

    private final CustomerService service;

    public CustomersDataFetcher(CustomerService service) {
        this.service = service;
    }

    @Override
    public List<Customer> get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        int limit = dataFetchingEnvironment.getArgument("limit");
        int offset = dataFetchingEnvironment.getArgument("offset");
        return service.getAllEntitiesPagination(offset, limit);
    }

}
