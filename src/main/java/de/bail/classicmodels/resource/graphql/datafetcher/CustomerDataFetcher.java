package de.bail.classicmodels.resource.graphql.datafetcher;

import de.bail.classicmodels.model.enities.Customer;
import de.bail.classicmodels.service.CustomerService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

/**
 * Customer Data Fetcher
 */
public class CustomerDataFetcher implements DataFetcher<Customer> {

    private final CustomerService service;

    public CustomerDataFetcher(CustomerService service) {
        this.service = service;
    }

    @Override
    public Customer get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        int id = dataFetchingEnvironment.getArgument("id");
        return service.getEntityById(id);
    }

}
