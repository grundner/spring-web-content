package biz.grundner.springframework.web.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Stephan Grundner
 */
@Controller
public class PageController {

    @Autowired
    private PageService pageService;

    private static boolean hasChildElements(Node node) {
        NodeList children = node.getChildNodes();
        if (children.getLength() == 0) {
            return false;
        }

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                return true;
            }
        }

        return false;
    }

    private void toMap(Node child, Map<String, Object> model) {
        String name = child.getNodeName();

        List<Object> values = (List<Object>) model.get(name);
        if (values == null) {
            values = new ArrayList<>();
            model.put(name, values);
        }

        Object value = toMap(model, child);

        values.add(value);
    }

    private Object toMap(Map<String, Object> parent, Node node) {
        Map<String, Object> map = new LinkedHashMap<>();

//        map.put("$parent", parent);
//        if (parent == null) {
//            map.put("$page", map);
//        } else {
//            map.put("$page", parent.get("$page"));
//        }
//
//        map.put("$name", node.getNodeName());

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            NamedNodeMap attributes = node.getAttributes();
            if (attributes != null) {
                for (int j = 0; j < attributes.getLength(); j++) {
                    Node attribute = attributes.item(j);
                    toMap(attribute, map);
                }
            }
        }

        NodeList children = node.getChildNodes();
        if (!hasChildElements(node)) {
            return node.getTextContent();
        } else {
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                String name = child.getNodeName();
                if (name.startsWith("#")) {
                    continue;
                }
                toMap(child, map);
            }
        }

        return map;
    }


    @GetMapping("page")
    public String resolve(@RequestParam(name = "file") Path file,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          Model model) throws IOException {

        Page page = pageService.findPageByFile(file);

        Object x = toMap(null, page.getDocument().getDocumentElement());
        model.addAllAttributes((Map) x);


        return "basicPage";
    }
}
