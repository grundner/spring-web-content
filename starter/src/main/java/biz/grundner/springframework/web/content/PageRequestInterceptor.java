package biz.grundner.springframework.web.content;

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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        UrlPathHelper urlPathHelper = new UrlPathHelper();
        String url = urlPathHelper.getServletPath(request);

        Page page = pageService.findPageByUrl(url);

        if (page != null) {

            RequestDispatcher dispatcher = request.getServletContext()
                    .getRequestDispatcher("/page?file=" + page.getFile());
            dispatcher.forward(request, response);
            return false;
        }

        return true;
    }
}
