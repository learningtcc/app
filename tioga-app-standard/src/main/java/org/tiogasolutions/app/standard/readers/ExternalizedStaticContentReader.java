package org.tiogasolutions.app.standard.readers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiogasolutions.dev.common.exceptions.ApiException;
import org.tiogasolutions.dev.common.exceptions.ApiNotFoundException;

import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExternalizedStaticContentReader implements StaticContentReader {

    private static final Logger logger = LoggerFactory.getLogger(StaticContentReader.class);

    private final Path rootPath;

    public ExternalizedStaticContentReader(String path) {
        this(Paths.get(path));
    }

    public ExternalizedStaticContentReader(Path rootPath) {
        this.rootPath = rootPath;
        logger.info("Reading static resources from: " + rootPath);
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
        Path fullPath = getPath(contentPath);
        return Files.exists(fullPath);
    }

    @Override
    public byte[] readContent(UriInfo uriInfo) {
        String contentPath = uriInfo.getPath();
        return readContent(contentPath);
    }

    @Override
    public byte[] readContent(String contentPath) {
        Path fullPath = getPath(contentPath);
        try {
            return Files.readAllBytes(fullPath);
        } catch (IOException e) {
            throw ApiException.badRequest("Error reading externalized static content " + fullPath);
        }
    }

    private Path getPath(String contentPath) {
        contentPath = contentPath.startsWith("/") ? contentPath.substring(1) : contentPath;
        return rootPath.resolve(contentPath);
    }
}
