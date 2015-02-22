package com.security.examples.queryBuilder;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.security.examples.URI;
import com.security.examples.queryBuilder.domain.vo.HandlerType;
import com.security.examples.queryBuilder.domain.vo.QueryTriple;
import com.security.examples.queryBuilder.handlers.Handler;
import com.security.examples.queryBuilder.handlers.HandlerStrategy;
import com.security.examples.queryBuilder.handlers.HandlerConstants;

/**
 * Created by trevor on 2/20/15.
 */
public class InsertQueryBuilder extends AbstractQueryBuilder{
    public InsertQueryBuilder() {
        this.cmd = new StringBuilder("INSERT INTO {");
        this.SUFFIX_PERIOD = "";
        LINE_SEPARATOR = "";

        footer = new StringBuilder();
        footer.append(" }");
    }

    public InsertQueryBuilder(String graphURI) {
        this.cmd = new StringBuilder("INSERT DATA {");
        cmd.append("GRAPH <");
        cmd.append(graphURI);
        cmd.append("> {");

        this.SUFFIX_PERIOD = "";
        LINE_SEPARATOR = "";
        footer = new StringBuilder();
        footer.append(" }");
        footer.append(" }");
    }

    public void appendSparqlTriple(String subject,String predicate, String object) {
        Handler handler = HandlerStrategy.getHandler(HandlerType.STRING);
        createTripleStatement(handler.convertTripleToQueryTriple(subject,predicate,object));
    }

    public void appendSparqlTriple(String subject,String predicate, char object) {
        Handler handler = HandlerStrategy.getHandler(HandlerType.CHAR);
        createTripleStatement(handler.convertTripleToQueryTriple(subject,predicate,object,XSDDatatype.XSDstring));
    }
    public void appendSparqlTriple(String subject,String predicate, boolean object) {
        Handler handler = HandlerStrategy.getHandler(HandlerType.BOOLEAN);
        createTripleStatement(handler.convertTripleToQueryTriple(subject,predicate,object,XSDDatatype.XSDboolean));
    }

    public void appendSparqlTriple(String subject,String predicate, long object){
        Handler handler = HandlerStrategy.getHandler(HandlerType.LONG);
        createTripleStatement(handler.convertTripleToQueryTriple(subject,predicate,object,XSDDatatype.XSDlong));
    }
    public void appendSparqlTriple(String subject,String predicate, int object){
        Handler handler = HandlerStrategy.getHandler(HandlerType.INT);
        createTripleStatement(handler.convertTripleToQueryTriple(subject,predicate,object,XSDDatatype.XSDint));
    }

    public void appendSparqlTriple(String subject,String predicate, float object){
        Handler handler = HandlerStrategy.getHandler(HandlerType.FLOAT);
        createTripleStatement(handler.convertTripleToQueryTriple(subject,predicate,object,XSDDatatype.XSDfloat));
    }

    public void appendSparqlTriple(String subject,String predicate, URI object){
        Handler handler = HandlerStrategy.getHandler(HandlerType.URI);
        createTripleStatement(handler.convertTripleToQueryTriple(subject,predicate,object));
    }

    public void appendReifiedTriple(String subject, String predicate, String object, String reifiedUri,String... items){
        Handler handler = HandlerStrategy.getHandler(HandlerType.STRING);
        QueryTriple triple = handler.convertTripleToQueryTriple(subject, predicate, object);
        createReifiedQuery(reifiedUri, triple, items);
    }
    public void appendReifiedTriple(String subject, String predicate, char object, String reifiedUri,String... items){
        Handler handler = HandlerStrategy.getHandler(HandlerType.CHAR);
        QueryTriple triple = handler.convertTripleToQueryTriple(subject, predicate, object);
        createReifiedQuery(reifiedUri, triple, items);
    }
    public void appendReifiedTriple(String subject, String predicate, int object, String reifiedUri,String... items){
        Handler handler = HandlerStrategy.getHandler(HandlerType.INT);
        QueryTriple triple = handler.convertTripleToQueryTriple(subject, predicate, object);
        createReifiedQuery(reifiedUri, triple, items);
    }
    public void appendReifiedTriple(String subject, String predicate, long object, String reifiedUri,String... items){
        Handler handler = HandlerStrategy.getHandler(HandlerType.LONG);
        QueryTriple triple = handler.convertTripleToQueryTriple(subject, predicate, object);
        createReifiedQuery(reifiedUri, triple, items);
    }
    public void appendReifiedTriple(String subject, String predicate, boolean object, String reifiedUri,String... items){
        Handler handler = HandlerStrategy.getHandler(HandlerType.BOOLEAN);
        QueryTriple triple = handler.convertTripleToQueryTriple(subject, predicate, object);
        createReifiedQuery(reifiedUri, triple, items);
    }

    public void appendReifiedTriple(String subject, String predicate, float object, String reifiedUri,String... items){
        Handler handler = HandlerStrategy.getHandler(HandlerType.FLOAT);
        QueryTriple triple = handler.convertTripleToQueryTriple(subject, predicate, object);
        createReifiedQuery(reifiedUri, triple, items);
    }

    public void appendReifiedTriple(String subject, String predicate, double object, String reifiedUri,String... items){
        Handler handler = HandlerStrategy.getHandler(HandlerType.DOUBLE);
        QueryTriple triple = handler.convertTripleToQueryTriple(subject, predicate, object);
        createReifiedQuery(reifiedUri, triple, items);
    }

    public void appendReifiedTriple(String subject, String predicate, URI object, String reifiedUri,String... items){
        Handler handler = HandlerStrategy.getHandler(HandlerType.URI);
        QueryTriple triple = handler.convertTripleToQueryTriple(subject, predicate, object);
        createReifiedQuery(reifiedUri, triple, items);
    }

    private void createReifiedQuery(String reifiedUri, QueryTriple triple, String[] items) {
        addDoubleLine();

        String appender = " ;";
        Node reifiedProperty = NodeFactory.createURI(reifiedUri);

        StringBuilder sb = new StringBuilder();

        sb.append("[] a ");
        sb.append(HandlerConstants.STATEMENT_URI);
        sb.append(appender);
        sb.append(System.lineSeparator());
        sb.append(HandlerConstants.SUBJECT_URI);
        sb.append(SPACE);
        sb.append(triple.getSubject());
        sb.append(appender);
        sb.append(System.lineSeparator());
        sb.append(HandlerConstants.PREDICATE_URI);
        sb.append(SPACE);
        sb.append(triple.getPredicate());
        sb.append(appender);
        sb.append(System.lineSeparator());
        sb.append(HandlerConstants.OBJECT_URI);
        sb.append(SPACE);
        sb.append(triple.getObject());
        sb.append(appender);
        sb.append(System.lineSeparator());
        sb.append(SparqlUtil.createUri(reifiedProperty.getURI()));
        String commandPrefix = "";

        for (String i : items) {
            sb.append(commandPrefix);
            sb.append("\"" + i + "\"");
            commandPrefix = ",";
        }

        cmd.append(sb.toString());

        addSuffixes();
    }

    /**
     * Takes a triple and converts it to a statement to be used
     * @param triple
     */
    private void createTripleStatement(QueryTriple triple) {
        addDoubleLine();
        cmd.append(triple.getSubject());
        cmd.append(SPACE);
        cmd.append(triple.getPredicate());
        cmd.append(SPACE);
        cmd.append(triple.getObject());
        addSuffixes();
    }

    @Override
    public String toQuery(){
        cmd.append(footer.toString());
        return cmd.toString();
    }
}