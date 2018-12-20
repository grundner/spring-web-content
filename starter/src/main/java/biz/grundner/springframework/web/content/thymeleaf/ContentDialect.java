package biz.grundner.springframework.web.content.thymeleaf;

import biz.grundner.springframework.web.content.thymeleaf.processor.ComponentIncludeProcessor;
import biz.grundner.springframework.web.content.thymeleaf.processor.ComponentReplaceProcessor;
import biz.grundner.springframework.web.content.thymeleaf.processor.UseTagProcessor;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.processor.IProcessor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Stephan Grundner
 */
public class ContentDialect extends AbstractProcessorDialect implements IExpressionObjectDialect {

    private static final String PREFIX = "co";

    public ComponentReplaceProcessor createReplaceProcessor() {
        return new ComponentReplaceProcessor(PREFIX, getDialectProcessorPrecedence() * 10);
    }

    public ComponentIncludeProcessor createIncludeProcessor() {
        return new ComponentIncludeProcessor(PREFIX, getDialectProcessorPrecedence() * 10 + 1);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        HashSet<IProcessor> processors = new HashSet<>();
        processors.add(createReplaceProcessor());
        processors.add(createIncludeProcessor());
        processors.add(new UseTagProcessor(PREFIX, getDialectProcessorPrecedence() * 10 + 2));

        return processors;
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return new IExpressionObjectFactory() {
            @Override
            public Set<String> getAllExpressionObjectNames() {
                return Collections.singleton("pages");
            }

            @Override
            public Object buildObject(IExpressionContext context, String expressionObjectName) {
                return new Pages();
            }

            @Override
            public boolean isCacheable(String expressionObjectName) {
                return true;
            }
        };
    }

    public ContentDialect() {
        super("Content dialect", PREFIX, 900);
    }
}
