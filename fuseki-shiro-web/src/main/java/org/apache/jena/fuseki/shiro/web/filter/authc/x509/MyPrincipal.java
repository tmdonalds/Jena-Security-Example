package org.apache.jena.fuseki.shiro.web.filter.authc.x509;

import java.util.Set;

/**
 * Created by trevor on 1/18/15.
 */
public class MyPrincipal {
    private final String username;
    private final Set<String> roles;

    public MyPrincipal(String username, Set<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public Set<String> getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        return "MyPrincipal{" +
                "username='" + username + '\'' +
                ", roles=" + roles +
                '}';
    }
}