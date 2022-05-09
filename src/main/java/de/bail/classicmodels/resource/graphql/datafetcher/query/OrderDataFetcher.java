package de.bail.classicmodels.resource.graphql.datafetcher.query;

import de.bail.classicmodels.model.enities.Order;
import de.bail.classicmodels.service.OrderService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

/**
 * Order Data Fetcher
 */
public class OrderDataFetcher implements DataFetcher<Order> {

    private final OrderService service;

    public OrderDataFetcher(OrderService service) {
        this.service = service;
    }

    @Override
    public Order get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        int id = dataFetchingEnvironment.getArgument("id");
        return service.getEntityById(id);
    }

}
