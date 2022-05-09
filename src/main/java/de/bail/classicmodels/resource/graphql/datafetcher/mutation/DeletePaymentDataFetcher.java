package de.bail.classicmodels.resource.graphql.datafetcher.mutation;

import de.bail.classicmodels.model.enities.Payment;
import de.bail.classicmodels.service.PaymentService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

/**
 * Delete Payment Data Fetcher
 */
public class DeletePaymentDataFetcher implements DataFetcher<Payment> {

    private final PaymentService service;

    public DeletePaymentDataFetcher(PaymentService service) {
        this.service = service;
    }

    @Override
    public Payment get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        String checkNumber = dataFetchingEnvironment.getArgument("checkNumber");
        int customerNumber = dataFetchingEnvironment.getArgument("customerNumber");
        return service.deleteById(new Payment.PaymentId(customerNumber, checkNumber));
    }

}
