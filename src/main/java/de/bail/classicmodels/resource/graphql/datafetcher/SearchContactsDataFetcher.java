package de.bail.classicmodels.resource.graphql.datafetcher;

import de.bail.classicmodels.model.enities.Contact;
import de.bail.classicmodels.service.CustomerService;
import de.bail.classicmodels.service.EmployeeService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Search Contacts Data Fetcher
 */
public class SearchContactsDataFetcher implements DataFetcher<List<Contact>> {

    private final CustomerService customerService;

    private final EmployeeService employeeService;

    public SearchContactsDataFetcher(CustomerService customerService, EmployeeService employeeService) {
        this.customerService = customerService;
        this.employeeService = employeeService;
    }

    @Override
    public List<Contact> get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        int limit = dataFetchingEnvironment.getArgument("limit");
        String term = dataFetchingEnvironment.getArgument("term");
        List<Contact> results = Stream.concat(
                customerService.search(term).stream(),
                employeeService.search(term).stream()
        ).collect(Collectors.toList());
        if (results.size() > limit) {
            return results.subList(0, limit);
        }
        return results;
    }

}
