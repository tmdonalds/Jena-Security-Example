package com.security.examples.queryBuilder.handlers;

import com.hp.hpl.jena.vocabulary.RDF;
import com.security.examples.queryBuilder.SparqlUtil;

/**
 * Created by trevor on 2/21/15.
 */
public class HandlerConstants {
    public static String STATEMENT_URI = SparqlUtil.createUri(RDF.Statement.getURI());
    public static String SUBJECT_URI = SparqlUtil.createUri(RDF.subject.getURI());
    public static String PREDICATE_URI = SparqlUtil.createUri(RDF.predicate.getURI());
    public static String OBJECT_URI = SparqlUtil.createUri(RDF.object.getURI());
}
