package biz.grundner.springframework.web.content;

import biz.grundner.springframework.web.content.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Enumeration;

/**
 * @author Stephan Grundner
 */
@Controller
public class PageController {

    @Autowired
    private PageService pageService;

    @Autowired
    private PageLoader pageLoader;

    @GetMapping("page")
    public String resolve(@RequestParam(name = "file") Path file,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          Model model) throws IOException {

        Page page = pageService.findPageByFile(file);

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
        model.addAttribute("$page", page);
        model.addAllAttributes(pageService.fromFragment(page));

        return page.getType();
    }
}
