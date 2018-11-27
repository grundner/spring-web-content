package biz.grundner.springframework.web.content.model;

import java.nio.file.Path;

/**
 * @author Stephan Grundner
 */
public class Page extends Fragment {

    private Path file;

    public Path getFile() {
        return file;
    }

    public void setFile(Path file) {
        this.file = file;
    }

    public Page() {
        super(null);
    }
}
