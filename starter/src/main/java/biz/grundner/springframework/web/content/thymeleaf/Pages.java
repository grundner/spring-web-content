package biz.grundner.springframework.web.content.thymeleaf;

import biz.grundner.springframework.web.content.PageService;
import biz.grundner.springframework.web.content.model.Page;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Stephan Grundner
 */
public class Pages {

    private static HttpServletRequest currentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

//    public static UrlService currentUrlService() {
//        HttpServletRequest request = currentRequest();
//        ApplicationContext applicationContext = RequestContextUtils.findWebApplicationContext(request);
//        return applicationContext.getBean(UrlService.class);
//    }

    public List<Object> findAllByType(String type, int limit, int offset) {
        HttpServletRequest request = currentRequest();
        ApplicationContext applicationContext = RequestContextUtils.findWebApplicationContext(request);
        PageService pageService = applicationContext.getBean(PageService.class);
        Collection<Page> pages = pageService.findPagesByType(type, limit, offset);

        return pages.stream()
                .map(pageService::toModel)
                .collect(Collectors.toList());
    }

    public List<Object> findAllByType(String type, int limit) {
        return findAllByType(type, limit, 0);
    }

    public List<Object> findAllByType(String type) {
        return findAllByType(type, 25);
    }
}
