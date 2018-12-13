package biz.grundner.springframework.web.content;

import biz.grundner.springframework.web.content.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Stephan Grundner
 */
public class PageRequestInterceptor implements HandlerInterceptor {

    public static final int ORDINAL = 100;

    @Autowired
    private PageService pageService;

    private final UrlPathHelper urlPathHelper = new UrlPathHelper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = urlPathHelper.getServletPath(request);
        Page page = pageService.findPageByURI(uri);

        if (page != null) {
            RequestDispatcher dispatcher = request.getServletContext()
                    .getRequestDispatcher("/page?file=" + page.getFile());
            dispatcher.forward(request, response);

            return false;
        }

        return true;
    }
}
