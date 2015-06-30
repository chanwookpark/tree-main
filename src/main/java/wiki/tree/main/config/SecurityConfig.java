package wiki.tree.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import wiki.tree.main.security.GoogleAccessTokenConverter;
import wiki.tree.main.security.GoogleTokenService;
import wiki.tree.main.security.MapClientTokenServices;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Autowired
    OAuth2RestTemplate oauthRestTemplate;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/doc/create", "/doc/edit/**", "/doc/save").authenticated()
                .anyRequest().permitAll()
            .and()
                .httpBasic()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(env.getProperty("security-oauth.google.loginUrl")))
            .and()
                .logout()
            .and()
                .anonymous()
            .and()
                .addFilterAfter(new OAuth2ClientContextFilter(), ExceptionTranslationFilter.class)
                .addFilterBefore(getOAuth2ProcessingFilter(), FilterSecurityInterceptor.class);
    }

    private OAuth2ClientAuthenticationProcessingFilter getOAuth2ProcessingFilter() {
        final OAuth2ClientAuthenticationProcessingFilter filter =
                new OAuth2ClientAuthenticationProcessingFilter(env.getProperty("security-oauth.google.loginUrl"));
        filter.setRestTemplate(oauthRestTemplate);
        filter.setTokenServices(googleTokenService());
        return filter;
    }

    @Bean
    public GoogleTokenService googleTokenService() {
        final GoogleTokenService s = new GoogleTokenService();
        s.setCheckTokenEndpointUrl(env.getProperty("security-oauth.google.checkPointUrl"));
        s.setClientId(env.getProperty("security-oauth.google.clientId"));
        s.setClientSecret(env.getProperty("security-oauth.google.secret"));
        s.setTokenName(env.getProperty("security-oauth.google.tokenName"));
        s.setAccessTokenConverter(new GoogleAccessTokenConverter());
        return s;
    }

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public OAuth2RestTemplate oauthTemplate(OAuth2ClientContext oauth2ClientContext) {
        final OAuth2RestTemplate template = new OAuth2RestTemplate(googleResource(), oauth2ClientContext);
        final AccessTokenProviderChain chain = new AccessTokenProviderChain(Arrays.<AccessTokenProvider>asList(
                new AuthorizationCodeAccessTokenProvider(), new ImplicitAccessTokenProvider(),
                new ResourceOwnerPasswordAccessTokenProvider(), new ClientCredentialsAccessTokenProvider()));
        chain.setClientTokenServices(clientTokenServices());
        template.setAccessTokenProvider(chain);
        return template;
    }

    @Bean
    public MapClientTokenServices clientTokenServices() {
        return new MapClientTokenServices();
    }

    private OAuth2ProtectedResourceDetails googleResource() {
        final ClientCredentialAuthorizationCodeResourceDetails resource = new ClientCredentialAuthorizationCodeResourceDetails();
        resource.setId(env.getProperty("security-oauth.google.id"));
        resource.setClientId(env.getProperty("security-oauth.google.clientId"));
        resource.setClientSecret(env.getProperty("security-oauth.google.secret"));
        resource.setAccessTokenUri(env.getProperty("security-oauth.google.accessTokenUri"));
        resource.setUserAuthorizationUri(env.getProperty("security-oauth.google.authUri"));
        resource.setTokenName(env.getProperty("security-oauth.google.tokenName"));
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

    public static class ClientCredentialAuthorizationCodeResourceDetails extends AuthorizationCodeResourceDetails {
        // 계정 정보나 role 구분 없이 인증만 원함
        @Override
        public boolean isClientOnly() {
            return true;
        }
    }

}
