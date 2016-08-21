package ru.aim.pilot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.nio.charset.Charset;
import java.util.Locale;

@SpringBootApplication
public class App extends SpringBootServletInitializer {

    public static final Locale DEFAULT_LOCALE = Locale.forLanguageTag("ru");

    /**
     * Comma-separated list of basenames, each following the ResourceBundle convention.
     * Essentially a fully-qualified classpath location. If it doesn't contain a package
     * qualifier (such as "org.mypackage"), it will be resolved from the classpath root.
     */
    private static final String BASENAME = "messages";

    /**
     * Message bundles encoding.
     */
    private Charset encoding = Charset.forName("UTF-8");

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(App.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(DEFAULT_LOCALE);
        return slr;
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        if (StringUtils.hasText(BASENAME)) {
            messageSource.setBasenames(StringUtils.commaDelimitedListToStringArray(
                    StringUtils.trimAllWhitespace(BASENAME)));
        }
        if (this.encoding != null) {
            messageSource.setDefaultEncoding(this.encoding.name());
        }
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setCacheSeconds(-1);
        messageSource.setAlwaysUseMessageFormat(false);
        return messageSource;
    }
}
