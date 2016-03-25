package org.tiogasolutions.app.standard.readers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiogasolutions.dev.common.IoUtils;
import org.tiogasolutions.dev.common.exceptions.ApiException;
import org.tiogasolutions.dev.common.exceptions.ApiNotFoundException;

import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.io.InputStream;

public class BundledStaticContentReader implements StaticContentReader {

    private static final Logger logger = LoggerFactory.getLogger(StaticContentReader.class);

    private final String classPathRoot;

    public BundledStaticContentReader(String classPathRoot) {
        this.classPathRoot = classPathRoot;
    }

    @Override
    public void assertExisting(UriInfo uriInfo) throws ApiNotFoundException {
        String contentPath = uriInfo.getPath();
        assertExisting(contentPath);
    }

    @Override
    public void assertExisting(String contentPath) throws ApiNotFoundException {
        if (isExisting(contentPath) == false) {
            throw ApiException.notFound("The resource does not exist: " + contentPath);
        }
    }

    @Override
    public boolean isExisting(UriInfo uriInfo) {
        String contentPath = uriInfo.getPath();
        return isExisting(contentPath);
    }

    @Override
    public boolean isExisting(String contentPath) {
        String resource = getResource(contentPath);
        return getClass().getResource(resource) != null;
    }

    @Override
    public byte[] readContent(UriInfo uriInfo) {
        String contentPath = uriInfo.getPath();
        return readContent(contentPath);
    }

    @Override
    public byte[] readContent(String contentPath) {
        String resource = getResource(contentPath);
        try {
            InputStream in = getClass().getResourceAsStream(resource);

            if (in == null && getClass().getResource(classPathRoot) == null) {
                String msg = String.format("Content root not found (%s). Build project to update resources.", classPathRoot);
                throw ApiException.notFound(msg);

            } else if (in == null) {
                String msg = String.format("The resource was not found: %s", contentPath);
                throw ApiException.notFound(msg);
            }

            return IoUtils.toBytes(in);

        } catch (IOException e) {
            throw ApiException.internalServerError("Error reading embedded static content: " + resource);
        }
    }

    public String getResource(String contentPath) {

        if (classPathRoot.endsWith("/") == false && contentPath.startsWith("/") == false) {
            return classPathRoot + "/" + contentPath;

        } else if (classPathRoot.endsWith("/") && contentPath.startsWith("/")) {
            return classPathRoot + contentPath.substring(1);

        } else {
            return classPathRoot + contentPath;
        }
    }
}
