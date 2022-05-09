package de.bail.classicmodels.resource.graphql.datafetcher.mutation;

import de.bail.classicmodels.model.enities.Office;
import de.bail.classicmodels.service.OfficeService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

/**
 * Delete Office Data Fetcher
 */
public class DeleteOfficeDataFetcher implements DataFetcher<Office> {

    private final OfficeService service;

    public DeleteOfficeDataFetcher(OfficeService service) {
        this.service = service;
    }

    @Override
    public Office get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        int id = dataFetchingEnvironment.getArgument("id");
        return service.deleteById(id);
    }

}
