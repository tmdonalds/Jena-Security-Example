package com.security.examples.queryBuilder.handlers;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.security.examples.queryBuilder.SparqlUtil;
import com.security.examples.queryBuilder.domain.vo.QueryTriple;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Created by trevor on 2/21/15.
 */
public class StringHandler extends AbstractHandler{

    public static final String ESCAPE_CHAR = "'''";

    @Override
    public QueryTriple convertTripleToQueryTriple(Triple triple) {
        String subjectUri   = SparqlUtil.createUri(triple.getSubject().getURI());
        String predicateUri = SparqlUtil.createUri(triple.getPredicate().getURI());
        Node object = triple.getObject();

        if(!XSDDatatype.XSDstring.equals(object.getLiteralDatatype())) {
            throw new IllegalArgumentException("StringHandler will only work on XSDstring");
        }

        String o = object.getLiteralValue().toString();
        o = ESCAPE_CHAR + StringEscapeUtils.escapeJava(o) + ESCAPE_CHAR;

        return QueryTriple.getBuilder().withSubject(subjectUri).withPredicate(predicateUri)
                .withObject(o).build();
    }

    @Override
    public Triple convertToTriple(String subject,String predicate, Object object) {
        if(!(object instanceof String)) {
            throw new IllegalArgumentException("object must be String");
        }
        String o = (String) object;
        return new Triple(NodeFactory.createURI(subject),
                NodeFactory.createURI(predicate),
                NodeFactory.createLiteral(o, XSDDatatype.XSDstring));
    }
}
