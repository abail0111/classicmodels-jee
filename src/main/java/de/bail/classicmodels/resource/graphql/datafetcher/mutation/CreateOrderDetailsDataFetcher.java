package de.bail.classicmodels.resource.graphql.datafetcher.mutation;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bail.classicmodels.model.enities.Order;
import de.bail.classicmodels.model.enities.OrderDetail;
import de.bail.classicmodels.service.OrderDetailService;
import de.bail.classicmodels.service.OrderService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.LinkedHashMap;

/**
 * Create Order Detail Data Fetcher
 */
public class CreateOrderDetailsDataFetcher implements DataFetcher<OrderDetail> {

    private final OrderDetailService service;

    private final ObjectMapper mapper = new ObjectMapper();

    public CreateOrderDetailsDataFetcher(OrderDetailService service) {
        this.service = service;
    }

    @Override
    public OrderDetail get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        LinkedHashMap<String, Object> orderDetailInput = dataFetchingEnvironment.getArgument("orderDetail");
        // map orderDetail object
        final OrderDetail orderDetail = mapper.convertValue(orderDetailInput, OrderDetail.class);
        // create orderDetail
        return service.create(orderDetail);
    }

}
