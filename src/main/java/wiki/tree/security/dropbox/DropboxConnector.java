package wiki.tree.security.dropbox;

import com.dropbox.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

import static wiki.tree.security.dropbox.DropboxConstants.*;

/**
 * @author chanwook
 */
@Controller
public class DropboxConnector {

    private final Logger logger = LoggerFactory.getLogger(DropboxConnector.class);

    @Autowired
    Environment env;

    @RequestMapping("/security/dropbox/connect")
    public void connect(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        final String APP_KEY = env.getProperty("security-oauth.dropbox.appkey");
        final String APP_SECRET = env.getProperty("security-oauth.dropbox.secret");
        final String CLIENT_ID = env.getProperty("security-oauth.dropbox.clientId");
        final String REDIRECT_URL = env.getProperty("security-oauth.dropbox.redirectUrl");
        final String CSRF_TOKEN_KEY = env.getProperty("security-oauth.dropbox.csrfTokenKey");

        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        DbxRequestConfig config = new DbxRequestConfig(CLIENT_ID, Locale.getDefault().toString());
        DbxStandardSessionStore sessionStore = new DbxStandardSessionStore(session, CSRF_TOKEN_KEY);
        DbxWebAuth webAuth = new DbxWebAuth(config, appInfo, REDIRECT_URL, sessionStore);

        session.setAttribute(SESSION_WEB_AUTH, webAuth);
        session.setAttribute(SESSION_REQUEST_CONFIG, config);
        session.setAttribute(SESSION_SESSION_STORE, sessionStore);

        final String callback = request.getParameter("callback");
        if (StringUtils.hasText(callback)) {
            session.setAttribute(SESSION_CALLBACK, callback);
        }

        final String authorizationUrl = webAuth.start();

        if (logger.isInfoEnabled()) {
            logger.info("Dropbox Authorization Start: " + authorizationUrl);
        }

        response.sendRedirect(authorizationUrl);
    }

    @RequestMapping("/security/dropbox/login")
    public String login(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {

        DbxWebAuth webAuth = (DbxWebAuth) session.getAttribute(SESSION_WEB_AUTH);
        DbxRequestConfig config = (DbxRequestConfig) session.getAttribute(SESSION_REQUEST_CONFIG);

        try {
            final DbxAuthFinish authResponse = webAuth.finish(request.getParameterMap());
            final String accessToken = authResponse.accessToken;
            final DbxClient dbxClient = new DbxClient(config, accessToken);

            session.setAttribute(SESSION_CLIENT_API, dbxClient);

        } catch (DbxWebAuth.BadRequestException ex) {
            logger.error("On /dropbox-auth-finish: Bad request: " + ex.getMessage());
            response.sendError(400);
            return "/error";
        } catch (DbxWebAuth.BadStateException ex) {
            // Send them back to the start of the auth flow.
            response.sendRedirect("http://my-server.com/dropbox-auth-start");
            return "/error";
        } catch (DbxWebAuth.CsrfException ex) {
            logger.error("On /dropbox-auth-finish: CSRF mismatch: " + ex.getMessage());
            return "/error";
        } catch (DbxWebAuth.NotApprovedException ex) {
            // When Dropbox asked "Do you want to allow this app to access your</b>
            // Dropbox account?", the user clicked "No".</b>
            //...
            return "/error";
        } catch (DbxWebAuth.ProviderException ex) {
            logger.error("On /dropbox-auth-finish: Auth failed: " + ex.getMessage());
            response.sendError(503, "Error communicating with Dropbox.");
            return "/error";
        } catch (DbxException ex) {
            logger.error("On /dropbox-auth-finish: Error getting token: " + ex.getMessage());
            response.sendError(503, "Error communicating with Dropbox.");
            return "/error";
        }

        if (session.getAttribute(SESSION_CALLBACK) != null) {
            return "redirect:" + session.getAttribute(SESSION_CALLBACK);
        }
        return "redirect:/home";
    }
}
