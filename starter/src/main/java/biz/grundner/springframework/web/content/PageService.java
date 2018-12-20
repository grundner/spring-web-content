package biz.grundner.springframework.web.content;

import biz.grundner.springframework.web.content.model.Fragment;
import biz.grundner.springframework.web.content.model.Page;
import biz.grundner.springframework.web.content.model.Payload;
import biz.grundner.springframework.web.content.model.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    public PageRepository getPageRepository() {
        return pageRepository;
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

    public String toString(Page page) {
        return String.format("%s{type=%s,file=\"%s\"}@%d",
                Page.class.getName(),
                page.getType(),
                page.getResource().toString(),
                System.identityHashCode(page));
    }

    public Map<String, Object> toModel(Payload payload) {
        Map<String, Object> model = new LinkedHashMap<>();

        if (payload instanceof Fragment) {
            Fragment fragment = (Fragment) payload;
            fragment.getSequences().forEach((name, sequence) -> {
                model.put(name, sequence.getPayloads().stream()
                        .map(this::toModel)
                        .collect(Collectors.toList()));
            });
        } else {
            Text text = (Text) payload;
            model.put("$text", text.getValue());
        }

        model.put("$payload", payload);

        return model;
    }

    public void reload() throws IOException {
        pageRepository.reload();
    }
}
