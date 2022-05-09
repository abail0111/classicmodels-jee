package de.bail.classicmodels.resource.graphql.datafetcher.mutation;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bail.classicmodels.model.enities.OrderDetail;
import de.bail.classicmodels.model.enities.Payment;
import de.bail.classicmodels.service.PaymentService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.LinkedHashMap;

/**
 * Create Payment Data Fetcher
 */
public class CreatePaymentDataFetcher implements DataFetcher<Payment> {

    private final PaymentService service;

    private final ObjectMapper mapper = new ObjectMapper();

    public CreatePaymentDataFetcher(PaymentService service) {
        this.service = service;
    }

    @Override
    public Payment get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        LinkedHashMap<String, Object> paymentInput = dataFetchingEnvironment.getArgument("payment");
        // map payment object
        final Payment payment = mapper.convertValue(paymentInput, Payment.class);
        // create payment
        return service.create(payment);
    }

}
