package org.apache.jena.fuseki.shiro.web.filter.authc.x509;

/**
 * Created by trevor on 1/19/15.
 */

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.iterator.Map1;
import org.apache.jena.security.SecurityEvaluator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.util.Collection;
import java.util.Set;

@SuppressWarnings("PackageAccessibility")
public class MyAppSecurityEvaluator implements SecurityEvaluator{
    private static Logger LOGGER = LoggerFactory.getLogger(MyAppSecurityEvaluator.class);
    private Principal principal;
    private Model model;
    private final Property myAppRole = ResourceFactory.createProperty("urn:myapp:role");

    public MyAppSecurityEvaluator(Model model){
        this.model = model;
    }

    @Override
    public boolean evaluate(Action action, SecNode secNode) {
        return true;
    }

    @Override
    public boolean evaluate(Set<Action> actions, SecNode secNode) {
        return true;
    }

    @Override
    public boolean evaluate(Action action, SecNode secNode, SecTriple secTriple) {
        if(SecTriple.ANY.equals(secTriple)){
            return false;
        }

        // call to Shiro to get the current user
        Subject currentUser = SecurityUtils.getSubject();
        // must set the session attribute in the web filter using session.setAttribute( "hasRoles", Boolean.TRUE ); if the user has any roles.
        Boolean hasRoles = (Boolean) currentUser.getSession().getAttribute( "hasRoles");
        if (hasRoles == null || ! hasRoles ) {
            return false;
        }

        Triple triple = EvaluatorUtil.convert(secTriple);
        Statement stmt = model.asStatement(triple);
        Resource resource = model.getAnyReifiedStatement(stmt);

        Collection<String> tripleRoles = resource.listProperties(myAppRole).mapWith(new Map1<Statement,String>(){
            @Override
            public String map1(Statement s){
                return s.getObject().toString();
            }
        }).toSet();

        if(tripleRoles.isEmpty()){
            LOGGER.warn("Didn't find any roles attached to this triple. Returning false.");
            return false;
        }

        try{
            currentUser.checkRoles( tripleRoles);
            LOGGER.info("Roles are valid");
        } catch(AuthorizationException ex){
            LOGGER.info("Roles are not valid");
            //if authorizationException is thrown return false
            //checkRoles method is a void method
            //https://shiro.apache.org/static/1.2.2/apidocs/org/apache/shiro/subject/Subject.html#checkRoles%28java.util.Collection%29
            //
            return false;
        }

        return true;
    }

    @Override
    public boolean evaluate(Set<Action> actions, SecNode secNode, SecTriple secTriple) {
        return true;
    }

    @Override
    public boolean evaluateAny(Set<Action> actions, SecNode secNode) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean evaluateAny(Set<Action> actions, SecNode secNode, SecTriple secTriple) {
        // TODO Auto-generated method stub
        return evaluate(Action.Read, secNode, secTriple);
    }

    @Override
    public boolean evaluateUpdate(SecNode secNode, SecTriple secTriple, SecTriple secTriple2) {
        LOGGER.info("checking update evaluation");
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public Principal getPrincipal() {
        // TODO Auto-generated method stub
        return principal;
    }
}
