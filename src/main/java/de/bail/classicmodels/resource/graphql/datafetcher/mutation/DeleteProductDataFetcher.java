package de.bail.classicmodels.resource.graphql.datafetcher.mutation;

import de.bail.classicmodels.model.enities.Product;
import de.bail.classicmodels.service.ProductService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

/**
 * Delete Product Data Fetcher
 */
public class DeleteProductDataFetcher implements DataFetcher<Product> {

    private final ProductService service;

    public DeleteProductDataFetcher(ProductService service) {
        this.service = service;
    }

    @Override
    public Product get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        String id = dataFetchingEnvironment.getArgument("id");
        return service.deleteById(id);
    }

}
