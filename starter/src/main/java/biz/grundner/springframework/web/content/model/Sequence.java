package biz.grundner.springframework.web.content.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Stephan Grundner
 */
public class Sequence {

    private final String name;

    private final List<Payload> payloads = new ArrayList<>();

    public String getName() {
        return name;
    }

    public List<Payload> getPayloads() {
        return Collections.unmodifiableList(payloads);
    }

    public boolean appendPayload(Payload payload) {
        if (payload instanceof Page) {
            throw new IllegalArgumentException();
        }

        if (payloads.add(payload)) {
            payload.setSequence(this);

            return true;
        }

        return false;
    }

    public Sequence(String name) {
        this.name = name;
    }
}
