package biz.grundner.springframework.web.content.model;

/**
 * @author Stephan Grundner
 */
public abstract class Payload {

    private Sequence sequence;

    public Sequence getSequence() {
        return sequence;
    }

    void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }

    public int getOrdinal() {
        return sequence.getPayloads().indexOf(this);
    }
}
