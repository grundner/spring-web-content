package biz.grundner.springframework.web.content.model;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Stephan Grundner
 */
public class Fragment extends Payload {

    private Set<Payload> payloads = new LinkedHashSet<>();

    public Map<String, Payload> getPayloads() {
        return payloads.stream().collect(Collectors
                .toMap(Payload::getName, Function.identity()));
    }

    public Fragment(String name) {
        super(name);
    }
}
