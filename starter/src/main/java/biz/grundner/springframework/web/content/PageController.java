package biz.grundner.springframework.web.content;

import biz.grundner.springframework.web.content.model.*;
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
import java.util.*;
import java.util.stream.Collectors;

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
    public String resolve(@RequestParam(name = "path") String uri,
                       HttpServletRequest request,
                       HttpServletResponse response,
                       Model model) throws IOException {

        Page page = (Page) request.getAttribute(Page.class.getName());
        if (page == null) {
            page = pageService.findPageByURI(uri);
        }

//        Enumeration<String> names = request.getParameterNames();
//        while (names.hasMoreElements()) {
//            String name = names.nextElement();
//            String[] values = request.getParameterValues(name);
//            if (values.length > 1) {
//                model.addAttribute("$" + name, values);
//            } else {
//                model.addAttribute("$" + name, values[0]);
//            }
//        }

        model.addAllAttributes(pageService.toModel(page));

        return page.getType();
    }
}
