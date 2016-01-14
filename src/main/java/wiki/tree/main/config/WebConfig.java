package wiki.tree.main.config;

import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.view.script.ScriptTemplateConfigurer;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Web 환경설정 JavaConfig
 *
 * @author chanwook
 */
@Configuration
@EnableSpringDataWebSupport
public class WebConfig extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter {

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.scriptTemplate().prefix("/templates/").suffix(".html");
    }

    @Bean
    public ScriptTemplateConfigurer scriptTemplateConfigurer() throws IOException {
        final ScriptTemplateConfigurer configurer = new ScriptTemplateConfigurer();
        configurer.setRenderFunction("render");
        configurer.setSharedEngine(true);

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        configurer.setEngine(engine);
        addScripts(configurer);

        return configurer;
    }

    private void addScripts(ScriptTemplateConfigurer configurer) throws IOException {
        List<String> scripts = new ArrayList<>();
        scripts.add("/static/template/js/dust.js");
        scripts.add("/static/template/js/polyfill.js");
        scripts.add("/META-INF/resources/webjars/dustjs-linkedin/2.6.1/dust-full.js");

        // TODO we needs refactoring if add STV.setScripts("folder")
        final ClassPathResource dir = new ClassPathResource("/templates/compiled");
        for (File f : dir.getFile().listFiles()) {
            scripts.add("/templates/compiled/" + f.getName());
        }
        configurer.setScripts(scripts.toArray(new String[scripts.size()]));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern("/templates/**")) {
            registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
        }

        if (!registry.hasMappingForPattern("/static/**")) {
            registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        }
    }

    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter f = new CharacterEncodingFilter();
        f.setEncoding("UTF-8");
        f.setForceEncoding(true);
        return f;
    }

}
