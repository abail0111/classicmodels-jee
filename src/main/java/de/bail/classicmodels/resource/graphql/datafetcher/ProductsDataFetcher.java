package de.bail.classicmodels.resource.graphql.datafetcher;

import de.bail.classicmodels.model.enities.Product;
import de.bail.classicmodels.service.ProductService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;

/**
 * Products Data Fetcher
 */
public class ProductsDataFetcher implements DataFetcher<List<Product>> {

    private final ProductService service;

    public ProductsDataFetcher(ProductService service) {
        this.service = service;
    }

    @Override
    public List<Product> get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        int limit = dataFetchingEnvironment.getArgument("limit");
        int offset = dataFetchingEnvironment.getArgument("offset");
        String productLine = dataFetchingEnvironment.getArgument("productLine");
        if (productLine != null) {
            return service.filterByProductLine(productLine, offset, limit);
        }
        return service.getAllEntitiesPagination(offset, limit);
    }

}
