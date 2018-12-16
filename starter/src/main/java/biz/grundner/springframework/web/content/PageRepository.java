package biz.grundner.springframework.web.content;

import biz.grundner.springframework.web.content.model.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
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

    public Set<Page> getPages() {
        return Collections.unmodifiableSet(pages);
    }

    @Deprecated
    public Set<String> getLocations() {
        return locations;
    }

    public Path getDirectory() {
        return directory;
    }

    public void setDirectory(Path directory) {
        if (!directory.isAbsolute()) {
            directory = directory.toAbsolutePath().normalize();
        }

        this.directory = directory;
    }

    public Collection<Page> findAllPages() {
        return Collections.unmodifiableCollection(pages);
    }

    public Page findPageByURI(String path) {
        if (StringUtils.hasLength(path) && path.endsWith("/")) {
            path = path + "index";
        }

        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        String fileName = path + ".xml";
        return pages.stream().filter(it -> {
            Path file = it.getFile();
            if (file.isAbsolute()) {
                file = directory.relativize(file);
            }

            return fileName.equals(file.toString());
        }).findFirst().orElse(null);
    }

    public Page findPageByFile(Path file) {
        if (!file.isAbsolute()) {
            return findPageByFile(directory.resolve(file));
        }

        return pages.stream().filter(it -> file.equals(it.getFile())).findFirst().orElse(null);
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
        Set<Page> tmp = new HashSet<>(getPages());

        Files.walk(directory).forEach(file -> {
            try {
                String fileName = file.getFileName().toString();
                if (Files.isRegularFile(file) && fileName.endsWith(".xml")) {
                    Page page = findPageByFile(file);
                    if (page != null) {
                        tmp.remove(page);

                        BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);
                        LocalDateTime modified = LocalDateTime.ofInstant(attributes.lastModifiedTime().toInstant(), ZoneOffset.UTC);

                        if (!page.getModified().equals(modified)) {
                            CollectionUtils.remove(pages, page);
                            page = pageLoader.loadPage(directory.resolve(file));
                            CollectionUtils.add(pages, page);
                            LOG.info("Updated page {}", toString(page));
                        }

                    } else {
                        page = pageLoader.loadPage(directory.resolve(file));
                        CollectionUtils.add(pages, page);
                        LOG.info("Created page {}", toString(page));
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        tmp.forEach(page -> {
            CollectionUtils.remove(pages, page);
            LOG.info("Deleted page {}", toString(page));
        });
    }

    private String toString(Page page) {
        return String.format("%s{type=%s,file=\"%s\"}@%d",
                Page.class.getName(),
                page.getType(),
                page.getFile(),
                System.identityHashCode(page));
    }
}
