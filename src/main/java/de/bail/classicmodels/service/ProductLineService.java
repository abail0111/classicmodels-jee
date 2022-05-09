package de.bail.classicmodels.service;

import de.bail.classicmodels.model.enities.ProductLine;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

/**
 * ProductLine Service
 */
@ApplicationScoped
public class ProductLineService extends CrudService<ProductLine, String> {

    /**
     * Call constructor of abstract crud service
     * The type of Entity is needed to secure the correct implementation of the JPA access methods
     */
    protected ProductLineService() {
        super(ProductLine.class);
    }

    /**
     * Save a new payment to the database.
     * @param productLine Valid payment object
     * @return persisted payment object
     */
    @Override
    @Transactional
    public ProductLine create(ProductLine productLine) {
        if (productLine != null) {
            save(productLine);
        }
        return productLine;
    }

}
