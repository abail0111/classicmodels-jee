package de.bail.classicmodels.resource.graphql.datafetcher;

import de.bail.classicmodels.model.enities.Payment;
import de.bail.classicmodels.service.PaymentService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

/**
 * Payment Data Fetcher
 */
public class PaymentDataFetcher implements DataFetcher<Payment> {

    private final PaymentService service;

    public PaymentDataFetcher(PaymentService service) {
        this.service = service;
    }

    @Override
    public Payment get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        String checkNumber = dataFetchingEnvironment.getArgument("checkNumber");
        int customerNumber = dataFetchingEnvironment.getArgument("customerNumber");
        return service.getEntityById(new Payment.PaymentId(customerNumber, checkNumber));
    }

}
