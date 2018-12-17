package biz.grundner.springframework.web.content.io;

import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Stephan Grundner
 */
public class ResourceLocationUtils {

    public static String getLocation(Resource resource, String enc) {
        String url;
        try {
            url = resource.getURL().toString();
            return URLDecoder.decode(url, enc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getLocation(Resource resource) {
        return getLocation(resource, "UTF-8");
    }

    public static String normalizeLocation(String location) {
        if (location.startsWith(ResourceUtils.FILE_URL_PREFIX)) {
            String uri = location.substring(ResourceUtils.FILE_URL_PREFIX.length(), location.length());

            Path path = Paths.get(uri);
            if (!path.isAbsolute()) {
                uri = path.toAbsolutePath().toString();
                return ResourceUtils.FILE_URL_PREFIX + uri;
            }
        }

        return location;
    }
}
