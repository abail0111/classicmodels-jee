package de.bail.classicmodels.resource.graphql.datafetcher;

import de.bail.classicmodels.model.enities.Customer;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class CustomerDataFetcher implements DataFetcher<Customer> {

    @Override
    public Customer get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        Customer customer = new Customer();
        customer.setCustomerName("Test");
        customer.setFirstName("ASda");
        customer.setLastName("Basdas");

        return customer;
    }
}
