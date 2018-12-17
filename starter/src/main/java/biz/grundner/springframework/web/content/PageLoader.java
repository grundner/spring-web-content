package biz.grundner.springframework.web.content;

import biz.grundner.springframework.web.content.model.Page;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * @author Stephan Grundner
 */
public interface PageLoader {

    Page loadPage(Resource resource) throws IOException;
}
