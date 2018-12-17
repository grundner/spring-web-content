package biz.grundner.springframework.web.content;

import biz.grundner.springframework.web.content.io.ResourceLocationUtils;
import biz.grundner.springframework.web.content.model.Page;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Stephan Grundner
 */
public class PageRepository {

    private static final Logger LOG = LoggerFactory.getLogger(PageRepository.class);

    @Autowired
    private PageLoader pageLoader;

    private String locationPrefix;
    private String locationSuffix;

    @Autowired
    private ResourcePatternResolver resourceLoader;

    private Set<Page> pages = new LinkedHashSet<>();

    public Set<Page> getPages() {
        return Collections.unmodifiableSet(pages);
    }

    public String getLocationPrefix() {
        return locationPrefix;
    }

    public void setLocationPrefix(String locationPrefix) {
        this.locationPrefix = locationPrefix;
    }

    public String getLocationSuffix() {
        return locationSuffix;
    }

    public void setLocationSuffix(String locationSuffix) {
        this.locationSuffix = locationSuffix;
    }

    public Collection<Page> findAllPages() {
        return Collections.unmodifiableCollection(pages);
    }

    private String getLocationPattern() {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String normalizedLocationPrefix = ResourceLocationUtils.normalizeLocation(locationPrefix);
        return pathMatcher.combine(normalizedLocationPrefix, "/**/*" + locationSuffix);
    }

    private String resolvePath(Resource resource) {
        String normalizedLocationPrefix =  ResourceLocationUtils.normalizeLocation(locationPrefix);
        Resource root = resourceLoader.getResource(normalizedLocationPrefix);

        String path;

        String resourceLocation = ResourceLocationUtils.getLocation(resource);
        String rootLocation = ResourceLocationUtils.getLocation(root);
        path = StringUtils.removeStart(
                resourceLocation,
                rootLocation);

        path = StringUtils.removeEnd(path, locationSuffix);

        return path;
    }

    public Page findPageByURI(String path) {
        return pages.stream()
                .filter(it -> resolvePath(it.getResource()).equals(path))
                .findFirst().orElse(null);
    }

    public Collection<Page> findPagesByType(String type, int limit, int offset) {
        return pages.stream()
                .filter(it -> type.equals(it.getType()))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Scheduled(fixedDelay = 1000 * 10)
    public void refresh() throws IOException {
        pages.clear();

        String locationPattern = getLocationPattern();
        Resource[] resources = resourceLoader.getResources(locationPattern);
        for (Resource resource : resources) {
            Page page = pageLoader.loadPage(resource);

            pages.add(page);
        }
    }
}
