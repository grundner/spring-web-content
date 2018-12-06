package biz.grundner.springframework.web.content;

import biz.grundner.springframework.web.content.model.Page;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Stephan Grundner
 */
public interface PageLoader {

    Page loadPage(Path file) throws IOException;
}
