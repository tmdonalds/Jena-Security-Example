package com.security.examples.queryBuilder.handlers;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.graph.Triple;
import com.security.examples.queryBuilder.domain.vo.QueryTriple;

/**
 * Created by trevor on 2/21/15.
 */
public interface Handler {
    public QueryTriple convertTripleToQueryTriple(Triple triple);
    public QueryTriple convertTripleToQueryTriple(String subject, String predicate, Object object);
    public QueryTriple convertTripleToQueryTriple(String subject, String predicate, Object object, XSDDatatype xsdDatatype);

    public Triple convertToTriple(String subject,String predicate, Object object);

    public Triple convertToTriple(String subject, String predicate, Object object, XSDDatatype xsdDatatype);
}
