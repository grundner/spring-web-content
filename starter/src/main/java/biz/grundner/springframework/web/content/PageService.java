package biz.grundner.springframework.web.content;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author Stephan Grundner
 */
@Service
public class PageService {

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private PageLoader pageLoader;

    public Path getRootPath() {
        return Paths.get("./content");
    }

    public PageRepository getPageRepository() {
        return pageRepository;
    }

    public Page load(Path file, boolean register) throws IOException {
        Page page = pageLoader.loadPage(file);

        if (register) {
            pageRepository.savePage(page);
        }

        return page;
    }

    public void loadAll(Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String filename = FilenameUtils.getName(file.toString());
                if (!filename.startsWith(".")
                        && filename.endsWith(".xml")) {
                    Page page = load(file, true);
                }

                return FileVisitResult.CONTINUE;
            }
        });
    }

    @PostConstruct
    private void init() throws IOException {
        loadAll(getRootPath());
    }

    public Page findPageByFile(Path file) throws IOException {

        Page page = pageRepository.findPageByFile(file);
        if (page == null && Files.exists(file)) {
            page = load(file,true);
        }

        return page;
    }

    public Page findPageByUrl(String url) throws IOException {
        if (url.startsWith("/")) {
            url = url.substring(1);
        }

        Path file = getRootPath().resolve(url + ".xml");
        Page page = findPageByFile(file);

        return page;
    }
}
