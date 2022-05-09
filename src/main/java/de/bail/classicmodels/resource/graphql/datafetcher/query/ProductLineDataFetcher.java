package de.bail.classicmodels.resource.graphql.datafetcher.query;

import de.bail.classicmodels.model.enities.ProductLine;
import de.bail.classicmodels.service.ProductLineService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

/**
 * ProductLine Data Fetcher
 */
public class ProductLineDataFetcher implements DataFetcher<ProductLine> {

    private final ProductLineService service;

    public ProductLineDataFetcher(ProductLineService service) {
        this.service = service;
    }

    @Override
    public ProductLine get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        String id = dataFetchingEnvironment.getArgument("id");
        return service.getEntityById(id);
    }

}
