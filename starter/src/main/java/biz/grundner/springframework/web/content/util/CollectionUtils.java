package biz.grundner.springframework.web.content.util;

import java.util.Collection;

/**
 * @author Stephan Grundner
 */
public class CollectionUtils {

    public static void remove(Collection<?> collection, Object o) {
        if (!collection.remove(o)) {
            throw new RuntimeException(String.format("Unable to remove %s", o));
        }
    }

    public static <E> void add(Collection<E> collection, E e) {
        if (!collection.add(e)) {
            throw new RuntimeException(String.format("Unable to add %s", e));
        }
    }
}
