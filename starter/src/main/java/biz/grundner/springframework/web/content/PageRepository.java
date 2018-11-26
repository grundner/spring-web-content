package biz.grundner.springframework.web.content;

import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

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
}
