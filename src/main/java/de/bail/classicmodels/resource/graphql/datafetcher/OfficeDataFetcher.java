package de.bail.classicmodels.resource.graphql.datafetcher;

import de.bail.classicmodels.model.enities.Office;
import de.bail.classicmodels.service.OfficeService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

/**
 * Office Data Fetcher
 */
public class OfficeDataFetcher implements DataFetcher<Office> {

    private final OfficeService service;

    public OfficeDataFetcher(OfficeService service) {
        this.service = service;
    }

    @Override
    public Office get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        int id = dataFetchingEnvironment.getArgument("id");
        return service.getEntityById(id);
    }

}
