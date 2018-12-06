package biz.grundner.springframework.web.content.model;

/**
 * @author Stephan Grundner
 */
public class Text extends Payload {

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
