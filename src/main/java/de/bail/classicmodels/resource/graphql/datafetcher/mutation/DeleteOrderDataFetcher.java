package de.bail.classicmodels.resource.graphql.datafetcher.mutation;

import de.bail.classicmodels.model.enities.Order;
import de.bail.classicmodels.service.OrderService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

/**
 * Delete Order Data Fetcher
 */
public class DeleteOrderDataFetcher implements DataFetcher<Order> {

    private final OrderService service;

    public DeleteOrderDataFetcher(OrderService service) {
        this.service = service;
    }

    @Override
    public Order get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        int id = dataFetchingEnvironment.getArgument("id");
        return service.deleteById(id);
    }

}
