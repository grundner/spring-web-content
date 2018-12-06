package biz.grundner.springframework.web.content.thymeleaf.processor;

import org.thymeleaf.templatemode.TemplateMode;

/**
 * @author Stephan Grundner
 */
public class ComponentIncludeProcessor extends AbstractContentProcessor {

    private static final String ELEMENT_NAME = "include";

    public ComponentIncludeProcessor(String dialectPrefix, int precedence) {
        super(TemplateMode.HTML, dialectPrefix, null, false, ELEMENT_NAME, true, precedence, false);
    }
}
