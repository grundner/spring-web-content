package biz.grundner.springframework.web.content;

import biz.grundner.springframework.web.content.model.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Stephan Grundner
 */
@Component
public class PageRepository {

    private Map<Path, Page> pages = new HashMap<>();

    public void savePage(Page page) {
        Page replaced = pages.put(page.getFile(), page);
    }

    public Page findPageByFile(Path file) {
        return pages.get(file);
    }

    @Deprecated
    public Collection<Page> findPagesByFilename(String pattern) {
        String newPattern = "./content/" + pattern;
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return pages.entrySet().stream()
                .filter(it -> pathMatcher.match(newPattern, it.getKey().toString()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public Collection<Page> findPagesByType(String type, int limit, int offset) {
        return pages.values().stream()
                .filter(it -> type.equals(it.getType()))

                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }
}
