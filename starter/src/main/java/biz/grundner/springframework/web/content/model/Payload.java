package biz.grundner.springframework.web.content.model;

/**
 * @author Stephan Grundner
 */
public abstract class Payload {

    private final String name;

    public String getName() {
        return name;
    }

    public Payload(String name) {
        this.name = name;
    }
}
