package com.security.examples.queryBuilder.handlers;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.security.examples.queryBuilder.SparqlUtil;
import com.security.examples.queryBuilder.domain.vo.QueryTriple;
import org.apache.commons.lang3.NotImplementedException;

/**
 * Created by trevor on 2/21/15.
 */
public abstract class AbstractHandler implements Handler {
    @Override
    public QueryTriple convertTripleToQueryTriple(Triple triple) {
        String subjectUri   = SparqlUtil.createUri(triple.getSubject().getURI());
        String predicateUri = SparqlUtil.createUri(triple.getPredicate().getURI());
        Node object = triple.getObject();
        Object literalValue = object.getLiteralValue();

        if(!object.getLiteralDatatype().getJavaClass().equals(literalValue.getClass())) {
            throw new IllegalArgumentException("class data types do not match");
        }

        String o = literalValue.toString();

        return QueryTriple.getBuilder().withSubject(subjectUri).withPredicate(predicateUri)
                .withObject(o).build();
    }

    @Override
    public Triple convertToTriple(String subject, String predicate, Object object) {
        throw new NotImplementedException("If you need this case, please create a separate handler");
    }

    @Override
    public QueryTriple convertTripleToQueryTriple(String subject, String predicate, Object object) {
        Triple t = convertToTriple(subject, predicate, object);
        return convertTripleToQueryTriple(t);
    }

    @Override
    public Triple convertToTriple(String subject, String predicate, Object object,XSDDatatype xsdDatatype){
        return new Triple(NodeFactory.createURI(subject),
                NodeFactory.createURI(predicate),
                NodeFactory.createLiteral(String.valueOf(object),xsdDatatype));
    }

    @Override
    public QueryTriple convertTripleToQueryTriple(String subject, String predicate, Object object, XSDDatatype xsdDatatype) {
        Triple t = convertToTriple(subject, predicate, object,xsdDatatype);
        return convertTripleToQueryTriple(t);
    }
}
