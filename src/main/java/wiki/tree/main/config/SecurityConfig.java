package wiki.tree.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author chanwook
 */
@Configuration
@EnableOAuth2Client
@EnableWebSecurity
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    Environment env;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/doc/create", "/doc/edit/**", "/doc/save").authenticated()
                .anyRequest().permitAll()
            .and()
                .formLogin();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user01").password("1234").roles("USER")
                .and()
                .withUser("user02").password("1234").roles("USER", "ADMIN");
    }

    @Bean
    public OAuth2RestTemplate oauthTemplate(OAuth2ClientContext oauth2ClientContext) {
        return new OAuth2RestTemplate(googleResource(), oauth2ClientContext);
    }

    private OAuth2ProtectedResourceDetails googleResource() {
        final AuthorizationCodeResourceDetails resource = new AuthorizationCodeResourceDetails();
        resource.setId(env.getProperty("security-oauth.google.id"));
        resource.setClientId(env.getProperty("security-oauth.google.clientId"));
        resource.setClientSecret(env.getProperty("security-oauth.google.secret"));
        resource.setAccessTokenUri(env.getProperty("security-oauth.google.accessTokenUri"));
        resource.setUserAuthorizationUri(env.getProperty("security-oauth.google.authUri"));
//        resource.setTokenName(env.getProperty("security-oauth.google.tokenName"));
        resource.setScope(createScope(env.getProperty("security-oauth.google.scope")));
        resource.setPreEstablishedRedirectUri(env.getProperty("security-oauth.google.redirectUrl"));
        resource.setUseCurrentUri(false);
        resource.setAuthenticationScheme(AuthenticationScheme.query);
        resource.setClientAuthenticationScheme(AuthenticationScheme.form);
        return resource;
    }

    private List<String> createScope(String scope) {
        List<String> list = new ArrayList<String>();
        Collections.addAll(list, scope.split(","));
        return list;
    }

}
