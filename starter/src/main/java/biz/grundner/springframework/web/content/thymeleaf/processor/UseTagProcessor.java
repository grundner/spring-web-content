package biz.grundner.springframework.web.content.thymeleaf.processor;

import biz.grundner.springframework.web.content.model.Fragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.*;
import org.thymeleaf.templatemode.TemplateMode;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Stephan Grundner
 */
public class UseTagProcessor extends AbstractAttributeTagProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(UseTagProcessor.class);

    private HttpServletRequest getRequest(ITemplateContext context) {
        return ((WebEngineContext) context).getRequest();
    }

    @Override
    protected void doProcess(ITemplateContext context,
                             IProcessableElementTag tag,
                             final AttributeName currentAttributeName,
                             String attributeValue,
                             IElementTagStructureHandler structureHandler) {

        IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(context.getConfiguration());
        IStandardExpression expression = expressionParser.parseExpression(context, attributeValue);
        Object result = expression.execute(context);

        if (result == null || NoOpToken.class.isAssignableFrom(result.getClass())) {
            return;
        }

        if (result instanceof Fragment) {
            Fragment fragment = (Fragment) result;

//            structureHandler.setLocalVariable("$self", map);
//            map.forEach(structureHandler::setLocalVariable);
            fragment.getSequences().values().forEach(sequence -> {
                structureHandler.setLocalVariable(sequence.getName(), sequence.getPayloads());
            });

            structureHandler.setLocalVariable("self", fragment);

        } else {
            throw new RuntimeException("Unable to resolve template");
        }
    }

    protected UseTagProcessor(TemplateMode templateMode, String dialectPrefix, String elementName, boolean prefixElementName, String attributeName, boolean prefixAttributeName, int precedence, boolean removeAttribute) {
        super(templateMode, dialectPrefix, elementName, prefixElementName, attributeName, prefixAttributeName, precedence, removeAttribute);
    }

    public UseTagProcessor(String dialectPrefix, int precedence) {
        this(TemplateMode.HTML, dialectPrefix, null, false, "use", true, precedence, false);
    }
}
