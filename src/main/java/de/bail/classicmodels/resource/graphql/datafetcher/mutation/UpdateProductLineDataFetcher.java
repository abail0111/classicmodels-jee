package de.bail.classicmodels.resource.graphql.datafetcher.mutation;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bail.classicmodels.model.enities.ProductLine;
import de.bail.classicmodels.service.ProductLineService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.LinkedHashMap;

/**
 * Update ProductLine Data Fetcher
 */
public class UpdateProductLineDataFetcher implements DataFetcher<ProductLine> {

    private final ProductLineService service;

    private final ObjectMapper mapper = new ObjectMapper();

    public UpdateProductLineDataFetcher(ProductLineService service) {
        this.service = service;
    }

    @Override
    public ProductLine get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        LinkedHashMap<String, Object> productLineInput = dataFetchingEnvironment.getArgument("productLine");
        // map productLine object
        final ProductLine productLine = mapper.convertValue(productLineInput, ProductLine.class);
        // create productLine
        return service.update(productLine);
    }

}
