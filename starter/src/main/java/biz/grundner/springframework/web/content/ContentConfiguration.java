package biz.grundner.springframework.web.content;

import biz.grundner.springframework.web.content.thymeleaf.ContentDialect;
import biz.grundner.springframework.web.content.xml.DOMPageLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Stephan Grundner
 */
@Configuration
public class ContentConfiguration implements WebMvcConfigurer {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    protected PageRequestInterceptor contentRequestInterceptor() {
        return new PageRequestInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        PageRequestInterceptor pageRequestInterceptor = applicationContext.getBean(PageRequestInterceptor.class);
        registry.addInterceptor(pageRequestInterceptor).order(PageRequestInterceptor.ORDINAL);
    }

    @Bean
    @ConditionalOnMissingBean(PageLoader.class)
    protected PageLoader pageLoader() {
        return new DOMPageLoader();
    }

    @Bean
    @ConditionalOnMissingBean
    protected ContentProperties contentProperties() {
        ContentProperties contentProperties = new ContentProperties();
        contentProperties.setLocationPrefix("file:./content/");
        contentProperties.setLocationSuffix(".xml");

        return contentProperties;
    }

    @Bean
    protected PageRepository pageRepository(ContentProperties contentProperties) {
        PageRepository pageRepository = new PageRepository();
        pageRepository.setLocationPrefix(contentProperties.getLocationPrefix());
        pageRepository.setLocationSuffix(contentProperties.getLocationSuffix());

        return pageRepository;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        ContentProperties contentProperties = applicationContext.getBean(ContentProperties.class);
        String locationPrefix = contentProperties.getLocationPrefix();

        if (!locationPrefix.endsWith("/")) {
            locationPrefix += "/";
        }
        registry
            .addResourceHandler("/**")
            .addResourceLocations(locationPrefix);
    }

    @Bean
    public ContentDialect contentDialect() {
        return new ContentDialect();
    }
}
