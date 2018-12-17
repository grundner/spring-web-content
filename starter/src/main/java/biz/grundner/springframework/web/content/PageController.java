package biz.grundner.springframework.web.content;

import biz.grundner.springframework.web.content.model.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author Stephan Grundner
 */
@Controller
public class PageController {

    private static final Logger LOG = LoggerFactory.getLogger(PageController.class);

    @Autowired
    private PageService pageService;

    @Autowired
    private PageLoader pageLoader;

    @GetMapping("page")
    @ResponseBody
    public Map<String, Object> resolve(@RequestParam(name = "path") String uri,
                       HttpServletRequest request,
                       HttpServletResponse response,
                       Model model) throws IOException {

        Page page = pageService.findPageByURI(uri);

        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String[] values = request.getParameterValues(name);
            if (values.length > 1) {
                model.addAttribute("$" + name, values);
            } else {
                model.addAttribute("$" + name, values[0]);
            }
        }

//        model.addAllAttributes(request.getParameterMap());
//        model.addAttribute("$page", page);
//        model.addAllAttributes(pageService.fromFragment(page));

        return pageService.fromFragment(page);
    }
}
