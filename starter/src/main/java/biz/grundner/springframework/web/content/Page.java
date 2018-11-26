package biz.grundner.springframework.web.content;

import org.w3c.dom.Document;

import java.nio.file.Path;

/**
 * @author Stephan Grundner
 */
public class Page {

    private Path file;
    private Document document;


    public Path getFile() {
        return file;
    }

    public void setFile(Path file) {
        this.file = file;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
