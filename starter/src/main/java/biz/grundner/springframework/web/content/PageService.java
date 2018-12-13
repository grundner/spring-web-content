package biz.grundner.springframework.web.content;

import biz.grundner.springframework.web.content.model.Fragment;
import biz.grundner.springframework.web.content.model.Page;
import biz.grundner.springframework.web.content.model.Text;
import org.apache.commons.collections4.map.AbstractMapDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
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

//    @Autowired
//    private PageFileFilter pageFileFilter;


    public PageRepository getPageRepository() {
        return pageRepository;
    }

    public Page findPageByFile(Path file) throws IOException {
        return pageRepository.findPageByFile(file);
    }

    public Page findPageByURI(String uri) throws IOException {
        return pageRepository.findPageByURI(uri);
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


}
