package de.bail.classicmodels.resource.graphql.datafetcher;

import de.bail.classicmodels.model.enities.Order;
import de.bail.classicmodels.service.OrderService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;

/**
 * Orders Data Fetcher
 */
public class OrdersDataFetcher implements DataFetcher<List<Order>> {

    private final OrderService service;

    public OrdersDataFetcher(OrderService service) {
        this.service = service;
    }

    @Override
    public List<Order> get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        int limit = dataFetchingEnvironment.getArgument("limit");
        int offset = dataFetchingEnvironment.getArgument("offset");
        String status = dataFetchingEnvironment.getArgument("status");
        if (status != null) {
            return service.filterByStatus(status, offset, limit);
        }
        return service.getAllEntitiesPagination(offset, limit);
    }

}
