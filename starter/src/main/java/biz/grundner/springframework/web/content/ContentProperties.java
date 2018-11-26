package biz.grundner.springframework.web.content;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;

/**
 * @author Stephan Grundner
 */
@ConfigurationProperties(prefix = "content", ignoreUnknownFields = false)
public class ContentProperties {

    private Path basePath;

    public Path getBasePath() {
        return basePath;
    }

    public void setBasePath(Path basePath) {
        this.basePath = basePath;
    }
}
