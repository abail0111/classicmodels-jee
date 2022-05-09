package de.bail.classicmodels.resource.graphql.datafetcher.mutation;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bail.classicmodels.model.enities.OrderDetail;
import de.bail.classicmodels.service.OrderDetailService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.LinkedHashMap;

/**
 * Update OrderDetail Data Fetcher
 */
public class UpdateOrderDetailsDataFetcher implements DataFetcher<OrderDetail> {

    private final OrderDetailService service;

    private final ObjectMapper mapper = new ObjectMapper();

    public UpdateOrderDetailsDataFetcher(OrderDetailService service) {
        this.service = service;
    }

    @Override
    public OrderDetail get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        LinkedHashMap<String, Object> orderDetailInput = dataFetchingEnvironment.getArgument("orderDetail");
        // map orderDetail object
        final OrderDetail orderDetail = mapper.convertValue(orderDetailInput, OrderDetail.class);
        // create orderDetail
        return service.update(orderDetail);
    }

}
