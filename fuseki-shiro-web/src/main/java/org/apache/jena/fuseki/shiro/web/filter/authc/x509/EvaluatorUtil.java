package org.apache.jena.fuseki.shiro.web.filter.authc.x509;

/**
 * Created by trevor on 1/19/15.
 */

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import org.apache.jena.security.SecurityEvaluator;

@SuppressWarnings("PackageAccessibility")
public class EvaluatorUtil {
    public static Node convert(final SecurityEvaluator.SecNode secNode){
        if(SecurityEvaluator.SecNode.ANY.equals(secNode)){
            return Node.ANY;
        }

        if(SecurityEvaluator.SecNode.Type.Literal.equals(secNode.getType())){
            return NodeFactory.createLiteral(secNode.getValue());
        }

        if(SecurityEvaluator.SecNode.Type.Anonymous.equals(secNode.getType())){
            return NodeFactory.createAnon();
        }

        if(SecurityEvaluator.SecNode.VARIABLE.equals(secNode.getType())){
            return NodeFactory.createVariable(secNode.getValue());
        }

        return NodeFactory.createURI(secNode.getValue());
    }

    public static Triple convert(final SecurityEvaluator.SecTriple secTriple){
        return new Triple(EvaluatorUtil.convert(secTriple.getSubject()),
                EvaluatorUtil.convert(secTriple.getPredicate()),
                EvaluatorUtil.convert(secTriple.getObject()));
    }
}