package biz.grundner.springframework.web.content.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Stephan Grundner
 */
public class MapUtils {

    public static <K, V> boolean add(Map<K, Object> map, K key, V value) {
        Object x = map.get(key);
        if (x instanceof List) {
            return ((List<V>) x).add(value);
        } else {
            List<V> list = new ArrayList<>();
            if (list.add(value)) {
                return map.put(key, list) == null;
            }

            return false;
        }
    }
}
