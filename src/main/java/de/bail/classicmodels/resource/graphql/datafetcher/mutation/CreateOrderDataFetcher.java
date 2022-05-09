package de.bail.classicmodels.resource.graphql.datafetcher.mutation;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bail.classicmodels.model.enities.Order;
import de.bail.classicmodels.service.OrderService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.LinkedHashMap;

/**
 * Create Order Data Fetcher
 */
public class CreateOrderDataFetcher implements DataFetcher<Order> {

    private final OrderService service;

    private final ObjectMapper mapper = new ObjectMapper();

    public CreateOrderDataFetcher(OrderService service) {
        this.service = service;
    }

    @Override
    public Order get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        LinkedHashMap<String, Object> orderInput = dataFetchingEnvironment.getArgument("order");
        // map order object
        final Order order = mapper.convertValue(orderInput, Order.class);
        // create order
        return service.create(order);
    }

}
