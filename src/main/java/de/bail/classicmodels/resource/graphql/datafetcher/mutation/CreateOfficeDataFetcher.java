package de.bail.classicmodels.resource.graphql.datafetcher.mutation;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bail.classicmodels.model.enities.Office;
import de.bail.classicmodels.service.OfficeService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.LinkedHashMap;

/**
 * Office Data Fetcher
 */
public class CreateOfficeDataFetcher implements DataFetcher<Office> {

    private final OfficeService service;

    private final ObjectMapper mapper = new ObjectMapper();

    public CreateOfficeDataFetcher(OfficeService service) {
        this.service = service;
    }

    @Override
    public Office get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        LinkedHashMap<String, Object> officeInput = dataFetchingEnvironment.getArgument("office");
        // map office object
        final Office office = mapper.convertValue(officeInput, Office.class);
        // create office
        return service.create(office);
    }

}
