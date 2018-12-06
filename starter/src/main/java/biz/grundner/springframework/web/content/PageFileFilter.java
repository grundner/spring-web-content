package biz.grundner.springframework.web.content;

import java.io.File;

/**
 * @author Stephan Grundner
 */
public interface PageFileFilter {

    boolean accept(File file);
}
