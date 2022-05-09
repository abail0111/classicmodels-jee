package de.bail.classicmodels.resource.graphql.datafetcher.mutation;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bail.classicmodels.model.enities.Product;
import de.bail.classicmodels.service.ProductService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.LinkedHashMap;

/**
 * Create Product Data Fetcher
 */
public class CreateProductDataFetcher implements DataFetcher<Product> {

    private final ProductService service;

    private final ObjectMapper mapper = new ObjectMapper();

    public CreateProductDataFetcher(ProductService service) {
        this.service = service;
    }

    @Override
    public Product get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        LinkedHashMap<String, Object> productInput = dataFetchingEnvironment.getArgument("product");
        // map product object
        final Product product = mapper.convertValue(productInput, Product.class);
        // create product
        return service.create(product);
    }

}
