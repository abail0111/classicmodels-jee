package de.bail.classicmodels.resource.graphql.datafetcher;

import de.bail.classicmodels.model.enities.Payment;
import de.bail.classicmodels.service.PaymentService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;

/**
 * Payments Data Fetcher
 */
public class PaymentsDataFetcher implements DataFetcher<List<Payment>> {

    private final PaymentService service;

    public PaymentsDataFetcher(PaymentService service) {
        this.service = service;
    }

    @Override
    public List<Payment> get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        int limit = dataFetchingEnvironment.getArgument("limit");
        int offset = dataFetchingEnvironment.getArgument("offset");
        return service.getAllEntitiesPagination(offset, limit);
    }

}
