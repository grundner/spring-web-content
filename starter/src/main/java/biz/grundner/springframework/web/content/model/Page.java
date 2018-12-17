package biz.grundner.springframework.web.content.model;

import org.springframework.core.io.Resource;

import java.time.LocalDateTime;

/**
 * @author Stephan Grundner
 */
public class Page extends Fragment {

    private String type;
    private Resource resource;

    private LocalDateTime created;
    private LocalDateTime modified;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
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
