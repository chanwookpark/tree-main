package wiki.tree.main.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chanwook
 */
public class MapClientTokenServices implements ClientTokenServices {
    private Map<Object, OAuth2AccessToken> tokenMap = new ConcurrentHashMap<Object, OAuth2AccessToken>();

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2ProtectedResourceDetails resource, Authentication authentication) {
        return tokenMap.get(authentication.getPrincipal());
    }

    @Override
    public void saveAccessToken(OAuth2ProtectedResourceDetails resource, Authentication authentication, OAuth2AccessToken accessToken) {
        tokenMap.put(authentication.getPrincipal(), accessToken);
    }

    @Override
    public void removeAccessToken(OAuth2ProtectedResourceDetails resource, Authentication authentication) {
        tokenMap.remove(authentication.getPrincipal());
    }
}
