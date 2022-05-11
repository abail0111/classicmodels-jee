package de.bail.classicmodels.service;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriBuilder;

/**
 * Link Service
 */
@ApplicationScoped
public class LinkService {

    protected String host = "localhost";

    protected String basePath = "classicmodels/api";

    protected String port = "8080";

    protected boolean sslEnabled = false;

    protected boolean relativeLinks = false;

    /**
     * Build new link
     * @param path Target path of the element to be linked
     * @param rel Link Relation Type according to RFC5988
     * @param contentType Content type of element to be linked
     * @return Link object
     */
    public Link buildLink(String path, String rel, String contentType) {
        if (relativeLinks) {
            return Link.fromUri(path)
                    .rel(rel)
                    .type(contentType)
                    .build();
        } else {
            return Link.fromUri(UriBuilder.fromUri(getBaseUri()).path(path).build().toString())
                    .rel(rel)
                    .type(contentType)
                    .build();
        }
    }

    /**
     * Build new link with Link Relation Type 'related'
     * @param path Target path of the element to be linked
     * @param contentType Content type of element to be linked
     * @return Link object
     */
    public Link BuildLinkRelated(String path, String contentType) {
        return buildLink(path, "related", contentType);
    }

    /**
     * Get base URI from Server
     * @return String representation of base URI
     */
    public String getBaseUri() {
        String http;
        if (sslEnabled) {
            http = "https://";
        } else {
            http = "http://";
        }
        UriBuilder uriBuilder = UriBuilder.fromUri(http + host + ":" + port + "/");
        if (basePath != null) {
            uriBuilder.path(basePath);
        }
        return uriBuilder.build().toString();
    }

    /**
     * Get Host
     * @return Host as string
     */
    public String getHost() {

        return host;
    }

    /**
     * Get Port
     * @return port as string
     */
    public String getPort() {
        return port;
    }

    /**
     * Is SSL enabled
     * @return true if ssl is enabled
     */
    public boolean isSslEnabled() {
        return sslEnabled;
    }
}
