package de.bail.classicmodels.resource.graphql.datafetcher.mutation;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bail.classicmodels.model.enities.Customer;
import de.bail.classicmodels.service.CustomerService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.LinkedHashMap;

/**
 * Update Customer Data Fetcher
 */
public class UpdateCustomerDataFetcher implements DataFetcher<Customer> {

    private final CustomerService service;

    private final ObjectMapper mapper = new ObjectMapper();

    public UpdateCustomerDataFetcher(CustomerService service) {
        this.service = service;
    }

    @Override
    public Customer get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        LinkedHashMap<String, Object> customerInput = dataFetchingEnvironment.getArgument("customer");
        // map customer object
        final Customer customer = mapper.convertValue(customerInput, Customer.class);
        // create customer
        return service.update(customer);
    }

}
