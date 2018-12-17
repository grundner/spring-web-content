package biz.grundner.springframework.web.content;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Stephan Grundner
 */
@ConfigurationProperties(prefix = "content", ignoreUnknownFields = false)
public class ContentProperties {

    private String locationPrefix;
    private String locationSuffix;

    public String getLocationPrefix() {
        return locationPrefix;
    }

    public void setLocationPrefix(String locationPrefix) {
        this.locationPrefix = locationPrefix;
    }

    public String getLocationSuffix() {
        return locationSuffix;
    }

    public void setLocationSuffix(String locationSuffix) {
        this.locationSuffix = locationSuffix;
    }
}
