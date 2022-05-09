package de.bail.classicmodels.resource.graphql.datafetcher.query;

import de.bail.classicmodels.model.enities.Product;
import de.bail.classicmodels.service.ProductService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

/**
 * Product Data Fetcher
 */
public class ProductDataFetcher implements DataFetcher<Product> {

    private final ProductService service;

    public ProductDataFetcher(ProductService service) {
        this.service = service;
    }

    @Override
    public Product get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        String id = dataFetchingEnvironment.getArgument("id");
        return service.getEntityById(id);
    }

}
