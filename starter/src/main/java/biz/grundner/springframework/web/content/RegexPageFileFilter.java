package biz.grundner.springframework.web.content;

import java.io.File;

/**
 * @author Stephan Grundner
 */
public class RegexPageFileFilter implements PageFileFilter {

    private final String regex;

    @Override
    public boolean accept(File file) {
        return file.getName().matches(regex);
    }

    public RegexPageFileFilter(String regex) {
        this.regex = regex;
    }
}
