package org.tiogasolutions.app.standard.readers;

import org.tiogasolutions.dev.common.exceptions.ApiNotFoundException;

import javax.ws.rs.core.UriInfo;

public class MockContentReader implements StaticContentReader {

    @Override
    public void assertExisting(UriInfo uriInfo) throws ApiNotFoundException {
    }

    @Override
    public void assertExisting(String contentPath) throws ApiNotFoundException {
    }

    @Override
    public boolean isExisting(UriInfo uriInfo) {
        return true;
    }

    @Override
    public boolean isExisting(String contentPath) {
        return true;
    }

    @Override
    public byte[] readContent(UriInfo uriInfo) {
        return "<html><body><h1>Hello World</h1><p>It's me, dummy site.</p></body></html>".getBytes();
    }

    @Override
    public byte[] readContent(String contentPath) {
        return "<html><body><h1>Hello World</h1><p>It's me, dummy site.</p></body></html>".getBytes();
    }
}
