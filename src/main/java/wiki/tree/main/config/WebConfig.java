package wiki.tree.main.config;

import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import r2.dustjs.spring.DustModel;
import r2.dustjs.spring.DustModelMapper;
import r2.dustjs.spring.DustRenderModelHandlerMethodArgumentResolver;
import r2.dustjs.spring.DustjsView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static r2.dustjs.spring.DustModel.*;

/**
 * Web 환경설정 JavaConfig
 *
 * @author chanwook
 */
@Configuration
@EnableSpringDataWebSupport
public class WebConfig extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new DustRenderModelHandlerMethodArgumentResolver());
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        //TODO 우선 동작하게..
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");
        viewResolver.setViewClass(DustjsView.class);
        viewResolver.setCache(false);
        final HashMap<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(MAPPER_KEY, new DustModelMapper() {
            @Override
            public void bind(DustModel dm, Map<String, Object> mergedOutputModel, HttpServletRequest request) {
                final Object csrf = request.getAttribute("_csrf");
                if (csrf != null) {
                    dm.put("_csrf", csrf);
                }
            }
        });

        viewResolver.setAttributesMap(attributes);
        registry.viewResolver(viewResolver);
    }

    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter f = new CharacterEncodingFilter();
        f.setEncoding("UTF-8");
        f.setForceEncoding(true);
        return f;
    }

}
