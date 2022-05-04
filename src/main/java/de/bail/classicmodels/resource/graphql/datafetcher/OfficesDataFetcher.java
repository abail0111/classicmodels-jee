package de.bail.classicmodels.resource.graphql.datafetcher;

import de.bail.classicmodels.model.enities.Office;
import de.bail.classicmodels.service.OfficeService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;

/**
 * Offices Data Fetcher
 */
public class OfficesDataFetcher implements DataFetcher<List<Office>> {

    private final OfficeService service;

    public OfficesDataFetcher(OfficeService service) {
        this.service = service;
    }

    @Override
    public List<Office> get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        return service.getAllEntities();
    }

}
