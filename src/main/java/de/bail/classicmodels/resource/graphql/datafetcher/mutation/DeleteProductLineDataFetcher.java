package de.bail.classicmodels.resource.graphql.datafetcher.mutation;

import de.bail.classicmodels.model.enities.ProductLine;
import de.bail.classicmodels.service.ProductLineService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

/**
 * Delete ProductLine Data Fetcher
 */
public class DeleteProductLineDataFetcher implements DataFetcher<ProductLine> {

    private final ProductLineService service;

    public DeleteProductLineDataFetcher(ProductLineService service) {
        this.service = service;
    }

    @Override
    public ProductLine get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        String id = dataFetchingEnvironment.getArgument("id");
        return service.deleteById(id);
    }

}
