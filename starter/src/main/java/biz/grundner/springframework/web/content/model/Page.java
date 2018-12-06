package biz.grundner.springframework.web.content.model;

import java.nio.file.Path;
import java.time.LocalDateTime;

/**
 * @author Stephan Grundner
 */
public class Page extends Fragment {

    private String type;
    private Path file;

    private LocalDateTime created;
    private LocalDateTime modified;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Path getFile() {
        return file;
    }

    public void setFile(Path file) {
        this.file = file;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }
}
