package de.bail.classicmodels.resource.graphql.datafetcher.mutation;

import de.bail.classicmodels.model.enities.Customer;
import de.bail.classicmodels.service.CustomerService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

/**
 * Delete Customer Data Fetcher
 */
public class DeleteCustomerDataFetcher implements DataFetcher<Customer> {

    private final CustomerService service;

    public DeleteCustomerDataFetcher(CustomerService service) {
        this.service = service;
    }

    @Override
    public Customer get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        int id = dataFetchingEnvironment.getArgument("id");
        return service.deleteById(id);
    }

}
