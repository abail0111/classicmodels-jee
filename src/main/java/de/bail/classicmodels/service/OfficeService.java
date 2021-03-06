package de.bail.classicmodels.service;

import de.bail.classicmodels.model.enities.Office;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

/**
 * Office Service
 */
@ApplicationScoped
public class OfficeService extends CrudService<Office, Integer> {

    /**
     * Call constructor of abstract crud service
     * The type of Entity is needed to secure the correct implementation of the JPA access methods
     */
    protected OfficeService() {
        super(Office.class);
    }

    /**
     * Save a new office to the database.
     * @param office Valid office object
     * @return persisted office object
     */
    @Override
    @Transactional
    public Office create(Office office) {
        if (office != null) {
            save(office);
        }
        return office;
    }

}
