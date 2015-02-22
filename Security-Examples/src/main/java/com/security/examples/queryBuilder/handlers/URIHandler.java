package com.security.examples.queryBuilder.handlers;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.security.examples.URI;
import com.security.examples.queryBuilder.SparqlUtil;
import com.security.examples.queryBuilder.domain.vo.QueryTriple;

/**
 * Created by trevor on 2/21/15.
 */
public class URIHandler extends AbstractHandler{
    @Override
    public QueryTriple convertTripleToQueryTriple(Triple triple) {
        Node object = triple.getObject();
        if(!object.isURI()){
            throw new IllegalArgumentException("URIHandler will only work on URI");
        }

        String subjectUri   = SparqlUtil.createUri(triple.getSubject().getURI());
        String predicateUri = SparqlUtil.createUri(triple.getPredicate().getURI());
        String o = SparqlUtil.createUri(object.getURI());

        return QueryTriple.getBuilder().withSubject(subjectUri).withPredicate(predicateUri)
                .withObject(o).build();
    }

    @Override
    public Triple convertToTriple(String subject,String predicate, Object object) {
        if(object instanceof String) {
            throw new IllegalArgumentException("object must be String");
        }
        URI o = (URI) object;

        return new Triple(NodeFactory.createURI(subject),
                NodeFactory.createURI(predicate),
                NodeFactory.createURI(o.getUri()));
    }
}
