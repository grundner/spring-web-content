package biz.grundner.springframework.web.content.thymeleaf.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.NoOpToken;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Stephan Grundner
 */
public abstract class AbstractContentProcessor extends AbstractAttributeTagProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractContentProcessor.class);

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

        if (result instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) result;
            String name = (String) map.get("$name");
            String template = name;

//            String template = resolveTemplate(self, tag);
            structureHandler.setLocalVariable("$self", map);
            map.forEach(structureHandler::setLocalVariable);

            HttpServletRequest request = getRequest(context);
//            FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);

            String newAttributeName = "th:" + currentAttributeName.getAttributeName();
            structureHandler.replaceAttribute(currentAttributeName, newAttributeName, template);
        } else {
            throw new RuntimeException("Unable to resolve template");
        }
    }

    protected AbstractContentProcessor(TemplateMode templateMode, String dialectPrefix, String elementName, boolean prefixElementName, String attributeName, boolean prefixAttributeName, int precedence, boolean removeAttribute) {
        super(templateMode, dialectPrefix, elementName, prefixElementName, attributeName, prefixAttributeName, precedence, removeAttribute);
    }
}
