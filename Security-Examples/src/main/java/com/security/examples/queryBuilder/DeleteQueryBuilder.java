package com.security.examples.queryBuilder;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * Created by trevor on 2/21/15.
 */
public class DeleteQueryBuilder extends AbstractQueryBuilder{
    public static final String OBJECT_VARIABLE = "?o";
    public static final String S_P_O_VARIABLES = "?s ?p ?o";
    public static final String SUBJECT_VARIABLE = "?s";
    private final boolean graphExist;
    private final String GRAPH_URI;

    public DeleteQueryBuilder() {
        this.graphExist = false;
        this.cmd = new StringBuilder("DELETE DATA {");
        this.footer = new StringBuilder();
        this.footer.append(" }");
        GRAPH_URI = null;
    }

    public DeleteQueryBuilder(String graphName) {
        this.graphExist = true;
        this.GRAPH_URI = SparqlUtil.createUri(graphName);
        this.cmd = new StringBuilder();
        this.footer = new StringBuilder();
        this.footer.append(" }");
    }

    private void addWithStatement() {
        if(graphExist) {
            cmd.append("WITH");
            cmd.append(GRAPH_URI);
        }
    }

    private void addHeader() {
        addWithStatement();
        cmd.append("DELETE { ");
        cmd.append(S_P_O_VARIABLES + " }");
        cmd.append(System.lineSeparator());
        cmd.append("WHERE {");
    }

    private void addFooter() {
        cmd.append(footer.toString());
    }

    /**
     * Deletes object uri
     * @param predicate
     * @param objectResource
     */
    public void appendDeleteByPredicateObject(String predicate, String objectResource) {
        addAppenderLine();
        addHeader();

        Node p = NodeFactory.createURI(predicate);
        Node o = NodeFactory.createURI(objectResource);

        String objectUri = SparqlUtil.createUri(o.getURI());
        String predicateUri = SparqlUtil.createUri(p.getURI());

        cmd.append(SUBJECT_VARIABLE);
        cmd.append(SPACE);
        cmd.append(predicateUri);
        cmd.append(SPACE);
        cmd.append(objectUri);

        addSuffixes();
        addSingleLine();

        cmd.append(S_P_O_VARIABLES);

        addFooter();
        appendReifiedPredicateObject(predicate, objectResource);
    }

    public void appendDeleteBySubjectPredicate(String subject, String predicate) {
        addAppenderLine();
        addHeader();

        Node s = NodeFactory.createURI(subject);
        Node p = NodeFactory.createURI(predicate);

        String subjectUri = SparqlUtil.createUri(s.getURI());
        String predicateUri = SparqlUtil.createUri(p.getURI());

        cmd.append(subjectUri);
        cmd.append(SPACE);
        cmd.append(predicateUri);
        cmd.append(SPACE);
        cmd.append(OBJECT_VARIABLE);

        addSuffixes();
        addSingleLine();

        cmd.append(S_P_O_VARIABLES);

        addFooter();
        appendReifiedSubjectPredicate(subject, predicate);
    }

    private void appendReifiedPredicateObject(String predicate, String objectResource) {
        addAppenderLine();
        addHeader();

        Node o = NodeFactory.createURI(objectResource);
        Node p = NodeFactory.createURI(predicate);

        String objectUri = SparqlUtil.createUri(o.getURI());
        String predicateUri = SparqlUtil.createUri(p.getURI());
        String rdfObjectUri = SparqlUtil.createUri(RDF.object.getURI());
        String rdfPredicateUri = SparqlUtil.createUri(RDF.predicate.getURI());

        cmd.append(SUBJECT_VARIABLE);
        cmd.append(SPACE);
        cmd.append(rdfPredicateUri);
        cmd.append(predicateUri);

        addSingleLine();

        cmd.append(SUBJECT_VARIABLE);
        cmd.append(SPACE);
        cmd.append(rdfObjectUri);
        cmd.append(objectUri);

        addSingleLine();

        cmd.append(S_P_O_VARIABLES);

        addFooter();
    }

    private void appendReifiedSubjectPredicate(String subject, String predicate) {
        addAppenderLine();
        addHeader();

        Node s = NodeFactory.createURI(subject);
        Node p = NodeFactory.createURI(predicate);

        String subjectUri = SparqlUtil.createUri(s.getURI());
        String predicateUri = SparqlUtil.createUri(p.getURI());
        String rdfSubjectUri = SparqlUtil.createUri(RDF.subject.getURI());
        String rdfPredicateUri = SparqlUtil.createUri(RDF.predicate.getURI());

        cmd.append(SUBJECT_VARIABLE);
        cmd.append(SPACE);
        cmd.append(rdfSubjectUri);
        cmd.append(subjectUri);

        addSingleLine();

        cmd.append(SUBJECT_VARIABLE);
        cmd.append(SPACE);
        cmd.append(rdfPredicateUri);
        cmd.append(predicateUri);

        addSingleLine();

        cmd.append(S_P_O_VARIABLES);

        addFooter();
    }

    @Override
    public String toQuery(){
        return cmd.toString();
    }
}