package de.bail.classicmodels.util;

import javax.servlet.ServletConfig;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

@ApplicationPath("api")
public class JaxRsActivator extends Application {

//    public JaxRsActivator(@Context ServletConfig servletConfig) {
//        super();
//        BeanConfig beanConfig = new BeanConfig();
//        beanConfig.setTitle("Classicmodels API");
//        beanConfig.setVersion("1.0.0");
//        beanConfig.setSchemes(new String[]{"http"});
//        beanConfig.setHost("localhost:8080");
//        beanConfig.setBasePath("/classicmodles");
//        beanConfig.setResourcePackage("de.bail.classicmodels.util");
//        beanConfig.setScan(true);
//    }
}
