package de.bail.classicmodels.resource.graphql.datafetcher.query;

import de.bail.classicmodels.model.enities.ProductLine;
import de.bail.classicmodels.service.ProductLineService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;

/**
 * ProductLines Data Fetcher
 */
public class ProductLinesDataFetcher implements DataFetcher<List<ProductLine>> {

    private final ProductLineService service;

    public ProductLinesDataFetcher(ProductLineService service) {
        this.service = service;
    }

    @Override
    public List<ProductLine> get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        return service.getAllEntities();
    }

}
