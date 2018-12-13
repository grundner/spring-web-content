package biz.grundner.springframework.web.content;

import biz.grundner.springframework.thumbor.FileLoader;
import biz.grundner.springframework.thumbor.ThumborRunner;
import biz.grundner.springframework.web.content.thymeleaf.ContentDialect;
import biz.grundner.springframework.web.content.xml.DOMPageLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        contentProperties.setBasePath(Paths.get("./content"));

        return contentProperties;
    }

//    @Bean
//    @ConditionalOnMissingBean(PageFileFilter.class)
//    protected PageFileFilter pageFileFilter() {
//        return new RegexPageFileFilter("^.*\\.xml$");
//    }

    @Bean
    protected PageRepository pageRepository(ContentProperties contentProperties) {
        PageRepository pageRepository = new PageRepository();
        pageRepository.setDirectory(contentProperties.getBasePath());

        return pageRepository;
    }

    @Bean
    @ConditionalOnMissingBean
    protected ThumborRunner thumborRunner(ContentProperties contentProperties) {
        ThumborRunner thumborRunner = new ThumborRunner();
        FileLoader fileLoader = new FileLoader();
        fileLoader.setRootPath(contentProperties.getBasePath());
        thumborRunner.setLoader(fileLoader);

        return thumborRunner;
    }

    @Bean
    public ResourceProperties resourceProperties(ResourceProperties resourceProperties, ContentProperties contentProperties) {
        List<String> staticLocations = new ArrayList<>(Arrays.asList(resourceProperties.getStaticLocations()));
        staticLocations.add(0,"file:" + contentProperties.toString());
        resourceProperties.setStaticLocations(staticLocations.toArray(new String[] {}));

        return resourceProperties;
    }

    @Bean
    public ContentDialect contentDialect() {
        return new ContentDialect();
    }
}
