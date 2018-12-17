package biz.grundner.springframework.web.content.xml;

import biz.grundner.springframework.web.content.PageLoader;
import biz.grundner.springframework.web.content.model.Fragment;
import biz.grundner.springframework.web.content.model.Page;
import biz.grundner.springframework.web.content.model.Payload;
import biz.grundner.springframework.web.content.model.Text;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Stephan Grundner
 */
@Component
public class DOMPageLoader implements PageLoader {

    private void toFragment(Node node, Fragment fragment) {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeName().startsWith("#")) {
                continue;
            }

            Payload payload = fromNode(child);
            fragment.appendPayload(child.getNodeName(), payload);
        }
    }

    private boolean hasChildElements(Node node) {
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

    private Payload fromNode(Node node) {
        if (node.getNodeType() == Node.DOCUMENT_NODE) {
            Element document = ((Document) node).getDocumentElement();
            Page page = new Page();
            page.setType(document.getNodeName());
            toFragment(document, page);

            return page;
        } else if (hasChildElements(node)) {
            Fragment fragment = new Fragment();
            toFragment(node, fragment);

            return fragment;
        } else {
            Text text = new Text();
            text.setValue(node.getTextContent());

            return text;
        }
    }

    @Override
    public Page loadPage(Resource resource) throws IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try (InputStream inputStream = resource.getInputStream()) {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(inputStream);

            Page page = (Page) fromNode(document);
            page.setResource(resource);

//            BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);
//            page.setCreated(LocalDateTime.ofInstant(attributes.creationTime().toInstant(), ZoneOffset.UTC));
//            page.setModified(LocalDateTime.ofInstant(attributes.lastModifiedTime().toInstant(), ZoneOffset.UTC));

            return page;
        } catch (SAXException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
