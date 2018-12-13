package biz.grundner.springframework.web.content;

import biz.grundner.springframework.web.content.model.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

    @Deprecated
    private final Set<String> locations = new LinkedHashSet<>();

    private Path directory;

    private Set<Page> pages = new LinkedHashSet<>();

    @Deprecated
    public Set<String> getLocations() {
        return locations;
    }

    public Path getDirectory() {
        return directory;
    }

    public void setDirectory(Path directory) {
        if (!directory.isAbsolute()) {
            try {
                directory = directory.toAbsolutePath().toRealPath();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.directory = directory;
    }

    public Collection<Page> findAllPages() {
        return Collections.unmodifiableCollection(pages);
    }

    public boolean savePage(Page page) {
        return pages.add(page);
    }

    public String toRelativePath(Path file) {

        if (file.isAbsolute()) {
//            file = file.relativize(directory);
            file = directory.relativize(file);
        }

        String path = file.toString();

        return path;
    }

    public Page findPageByURI(String path) {
        if (StringUtils.hasLength(path) && path.endsWith("/")) {
            path = path + "index";
        }

        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        String fileName = path + ".xml";
        return pages.stream().filter(it -> toRelativePath(it.getFile()).equals(fileName)).findFirst().orElse(null);
    }

    public Page findPageByFile(Path file) {
        return pages.stream().filter(it -> file.equals(it.getFile())).findFirst().orElse(null);
    }

    public Collection<Page> findPagesByType(String type, int limit, int offset) {
        return pages.stream()
                .filter(it -> type.equals(it.getType()))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @PostConstruct
    public void refresh() throws IOException {
        pages.clear();

        Files.walk(directory).forEach(file -> {
            try {
                String fileName = file.getFileName().toString();
                if (Files.isRegularFile(file) && fileName.endsWith(".xml")) {
                    Page page = findPageByFile(file);
                    if (page != null) {
                        BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);
                        LocalDateTime modified = LocalDateTime.ofInstant(attributes.lastModifiedTime().toInstant(), ZoneOffset.UTC);

                        if (!page.getModified().equals(modified)) {
                            CollectionUtils.remove(pages, page);
                            page = pageLoader.loadPage(file);
                            CollectionUtils.add(pages, page);
                        }

                    } else {
                        page = pageLoader.loadPage(file);
                        pages.add(page);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
