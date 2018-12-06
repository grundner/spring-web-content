package biz.grundner.springframework.web.content.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Stephan Grundner
 */
public class Fragment extends Payload {

    private Map<String, Sequence> sequences = new LinkedHashMap<>();

    public Map<String, Sequence> getSequences() {
        return Collections.unmodifiableMap(sequences);
    }

    public boolean appendPayload(String name, Payload payload) {
        Sequence sequence = sequences.get(name);
        if (sequence == null) {
            sequence = new Sequence(name);
            sequences.put(name, sequence);
        }

        return sequence.appendPayload(payload);
    }
}
