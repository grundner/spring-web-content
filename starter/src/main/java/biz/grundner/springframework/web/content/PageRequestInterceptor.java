package biz.grundner.springframework.web.content;

import biz.grundner.springframework.web.content.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Path;

/**
 * @author Stephan Grundner
 */
public class PageRequestInterceptor implements HandlerInterceptor {

    public static final int ORDINAL = 100;

    @Autowired
    private PageService pageService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        UrlPathHelper urlPathHelper = new UrlPathHelper();
        String url = urlPathHelper.getServletPath(request);

        Page page = pageService.findPageByUrl(url);

        if (page != null) {

            Path file = page.getFile();
            RequestDispatcher dispatcher = request.getServletContext()
                    .getRequestDispatcher("/page?file=" + file);
            dispatcher.forward(request, response);
            return false;
        }

        return true;
    }
}
