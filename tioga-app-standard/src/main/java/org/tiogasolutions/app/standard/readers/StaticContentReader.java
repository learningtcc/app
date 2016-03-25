package org.tiogasolutions.app.standard.readers;

import org.tiogasolutions.dev.common.exceptions.ApiNotFoundException;

import javax.ws.rs.core.UriInfo;

public interface StaticContentReader {

    void assertExisting(UriInfo uriInfo) throws ApiNotFoundException;
    void assertExisting(String contentPath) throws ApiNotFoundException;

    boolean isExisting(UriInfo uriInfo);
    boolean isExisting(String contentPath);

    byte[] readContent(UriInfo uriInfo);
    byte[] readContent(String contentPath);
}
