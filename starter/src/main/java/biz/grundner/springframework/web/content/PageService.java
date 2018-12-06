package biz.grundner.springframework.web.content;

import biz.grundner.springframework.web.content.model.Fragment;
import biz.grundner.springframework.web.content.model.Page;
import biz.grundner.springframework.web.content.model.Text;
import org.apache.commons.collections4.map.AbstractMapDecorator;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Stephan Grundner
 */
@Service
public class PageService {

    @Autowired
    private ContentProperties contentProperties;

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private PageLoader pageLoader;

    @Autowired
    private PageFileFilter pageFileFilter;

    public Path getRootPath() {
        return contentProperties.getBasePath();
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

    public Page load(File file, boolean register) throws IOException {
        return load(file.toPath(), register);
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
        if (!Files.exists(file)) {
            if (!StringUtils.isEmpty(url)) {
                url += "/";
            }
            file = getRootPath().resolve(url + "index.xml");
        }
        Page page = findPageByFile(file);

        return page;
    }

    @Deprecated
    public Collection<Page> findPagesByFilename(String pattern) {
        return pageRepository.findPagesByFilename(pattern);
    }

    public Collection<Page> findPagesByType(String type, int limit, int offset) {
        return pageRepository.findPagesByType(type, limit, offset);
    }

    public Collection<Page> findPagesByType(String type, int limit) {
        return findPagesByType(type, limit, 0);
    }

    public Map<String, Object> fromFragment(Fragment fragment) {
        Map<String, Object> map = new LinkedHashMap<>();

        fragment.getSequences().values().forEach(sequence -> {
//            map.put("$name", sequence.getName());

            sequence.getPayloads().forEach(payload -> {
                if (payload instanceof Text) {
//                    map.add(sequence.getName(), ((Text) payload).getValue());
                    MapUtils.add(map, sequence.getName(), ((Text) payload).getValue());
                } else {
//                    map.add(sequence.getName(), fromFragment((Fragment) payload));
                    Map<String, Object> x = fromFragment((Fragment) payload);
                    x.put("$name", sequence.getName());
                    x.put("$parent", new AbstractMapDecorator(map) {
                        @Override
                        public String toString() {
                            return map.getClass().getName() + "@" + System.identityHashCode(map);
                        }
                    });
                    MapUtils.add(map, sequence.getName(), x);


                }
            });

            Object j = map.get(sequence.getName());
            map.put("$children", j);
        });

        return map;
    }

    public Object toModel(Page page) {
        return fromFragment(page);
    }

    @PostConstruct
    private void init() throws IOException {
        FileAlterationObserver observer = new FileAlterationObserver(getRootPath().toFile());
        FileAlterationMonitor monitor = new FileAlterationMonitor(5000);

        try {
            monitor.start();
            observer.addListener(new FileAlterationListenerAdaptor() {
                @Override
                public void onFileCreate(File file) {
                    onFileChange(file);
                }

                @Override
                public void onFileChange(File file) {
                    if (pageFileFilter.accept(file)) {
                        try {
                            load(file, true);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            monitor.addObserver(observer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
