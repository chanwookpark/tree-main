package wiki.tree.main.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import r2.dustjs.spring.DustRenderModelHandlerMethodArgumentResolver;
import r2.dustjs.spring.DustjsView;

import java.util.List;

/**
 * Web 환경설정 JavaConfig
 *
 * @author chanwook
 */
@Configuration
@EnableSpringDataWebSupport
public class WebConfig extends WebMvcConfigurerAdapter {
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

        registry.viewResolver(viewResolver);
    }

}
