package org.apache.jena.fuseki.shiro.web.filter.authc.x509;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by trevor on 1/18/15.
 */
public class MyCustomService {
    private final Map<String, Set<String>> roleMap;
    private final static Logger LOGGER = LoggerFactory.getLogger(MyCustomService.class);

    MyCustomService(){
        roleMap = MyCustomService.fetchRoles();
    }

    public MyPrincipal findPrincipalByDn(String username) {
        LOGGER.info(String.format("fetching user roles for username %s",username));
        Set<String> roles = roleMap.get(username);

        if(roles == null || roles.isEmpty()){
            LOGGER.info("roles are empty for user [{}]",username);
            return null;
        } else{
            return new MyPrincipal(username,roles);
        }
    }

    private static Map<String,Set<String>> fetchRoles(){
        Map<String,Set<String>> map = new HashMap<>();
        Set<String> userRoles = new HashSet<>();
        userRoles.add("ROLE_A");
        userRoles.add("ROLE_B");
        userRoles.add("ROLE_C");

        Set<String> defaultRoles = new HashSet<>();
        defaultRoles.add("ROLE_D");

        map.put("testUser", userRoles);
        map.put("default", userRoles);

        return map;
    }
}
