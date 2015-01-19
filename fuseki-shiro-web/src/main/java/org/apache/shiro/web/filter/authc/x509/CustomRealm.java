package org.apache.shiro.web.filter.authc.x509;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;

/**
 * Created by trevor on 1/18/15.
 */
public class CustomRealm extends AuthorizingRealm {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomRealm.class);
    private final MyCustomService myCustomService = new MyCustomService();

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        UsernamePasswordToken token = (UsernamePasswordToken) principalCollection.getPrimaryPrincipal();

        String username = token.getUsername();

        // Find the thing that stores your user's roles.
        MyPrincipal principal = myCustomService.findPrincipalByDn(token.getUsername());

        if (principal == null) {
            LOGGER.info("Principal not found for authorizing user with username: "
                        + username);
            return null;
        } else {
            LOGGER.info(String.format(
                    "Authoriziong user %s with roles: %s", username,
                    principal.getRoles()));

            SimpleAuthorizationInfo result = new SimpleAuthorizationInfo(
                    principal.getRoles());
            return result;
        }
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        if (usernamePasswordToken.getUsername() == null
                || usernamePasswordToken.getUsername().isEmpty()) {
            throw new AuthenticationException("Authentication failed");
        }

        // Find the thing that stores your user's credentials. This may be the
        // same or different than
        // the thing that stores the roles.
        MyPrincipal principal = myCustomService
                .findPrincipalByDn(usernamePasswordToken.getUsername());
        if (principal == null) {
            LOGGER.info("Principal not found for user with username: "
                    + usernamePasswordToken.getUsername());
            return null;
        }

        LOGGER.info("Principal found for authenticating user with username: "
                + usernamePasswordToken.getUsername());

        LOGGER.info(principal.toString());

        return new SimpleAccount(principal.getUsername(),
                X509AuthenticationFilter.PASSWORD_PLACEHOLDER, getName(), principal.getRoles(),
                new HashSet());
    }
}
