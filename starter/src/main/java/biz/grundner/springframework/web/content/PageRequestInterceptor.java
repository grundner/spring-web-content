package biz.grundner.springframework.web.content;

import biz.grundner.springframework.web.content.model.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Stephan Grundner
 */
public class PageRequestInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(PageRequestInterceptor.class);

    public static final int ORDINAL = 100;

    @Autowired
    private PageService pageService;

    private final UrlPathHelper urlPathHelper = new UrlPathHelper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = urlPathHelper.getServletPath(request);
        Page page = pageService.findPageByURI(uri);

        if (page != null) {
            LOG.info("Page found: {}", pageService.toString(page));
            Resource resource = page.getResource();
            RequestDispatcher dispatcher = request.getServletContext()
                    .getRequestDispatcher("/page?path=" + uri);
            dispatcher.forward(request, response);

            return false;
        }

        return true;
    }
}
