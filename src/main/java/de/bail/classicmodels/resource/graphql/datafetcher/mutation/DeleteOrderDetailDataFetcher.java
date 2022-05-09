package de.bail.classicmodels.resource.graphql.datafetcher.mutation;

import de.bail.classicmodels.model.enities.OrderDetail;
import de.bail.classicmodels.service.OrderDetailService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

/**
 * Delete OrderDetail Data Fetcher
 */
public class DeleteOrderDetailDataFetcher implements DataFetcher<OrderDetail> {

    private final OrderDetailService service;

    public DeleteOrderDetailDataFetcher(OrderDetailService service) {
        this.service = service;
    }

    @Override
    public OrderDetail get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        int order = dataFetchingEnvironment.getArgument("order");
        String product = dataFetchingEnvironment.getArgument("product");
        return service.deleteById(new OrderDetail.OrderDetailId(order, product));
    }

}
