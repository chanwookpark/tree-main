package wiki.tree.main.security.google;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

import java.util.*;

/**
 * @author chanwook
 */
//FIXME 임시로 복사해 개발. 스프링 지라 이슈 확인 후 조치하자..
public class GoogleAccessTokenConverter extends DefaultAccessTokenConverter {

    private UserAuthenticationConverter userTokenConverter = new DefaultUserAuthenticationConverter();

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        Map<String, String> parameters = new HashMap<String, String>();
        @SuppressWarnings("unchecked")
        Set<String> scope =
                new LinkedHashSet<String>(map.containsKey(SCOPE) ?
                        /** Add code **/convertScopeToMap(map)/** Add code **/ : Collections.<String>emptySet());

        Authentication user = userTokenConverter.extractAuthentication(map);
        String clientId = (String) map.get(CLIENT_ID);
        parameters.put(CLIENT_ID, clientId);

        @SuppressWarnings("unchecked")
        Set<String> resourceIds = new LinkedHashSet<String>(map.containsKey(AUD) ? (Collection<String>) map.get(AUD)
                : Collections.<String>emptySet());
        OAuth2Request request = new OAuth2Request(parameters, clientId, null, true, scope, resourceIds, null, null,
                null);

        return new OAuth2Authentication(request, user);
    }

    /** Add code **/
    private Collection<String> convertScopeToMap(Map<String, ?> map) {
        final LinkedHashSet<String> scopeSet = new LinkedHashSet<String>();
        String scopeText = (String) map.get(SCOPE);
        Collections.addAll(scopeSet, scopeText.split(" "));
        return scopeSet;
    }
    /** Add code **/
}
