package com.security.examples;

import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
import com.security.examples.queryBuilder.DeleteQueryBuilder;
import com.security.examples.queryBuilder.InsertQueryBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.StopWatch;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by trevor on 2/20/15.
 */
public class SparqlTester {
    public static final String GRAPH_NAME = "http://example.org/SM_GRAPH";
    public static final String ENDPOINT_UPDATE = "http://localhost:3030/myapp/update";
    public static final String ENDPOINT_SELECT = "http://localhost:3030/myapp/sparql";

    public static void main(String[] args) throws IOException {
//        insertTriplesIntoDataset();

        deleteTriples();
        insertTriples();

//        String query = "\n" +
//                "\n" +
//                "SELECT ?object\n" +
//                "where {\n" +
//                "  GRAPH ?g{\n" +
//                "  ?subject ?predicate ?object\n" +
//                "  }\n" +
//                "}\n" +
//                "LIMIT 25";
//
//        QueryExecution qexec = QueryExecutionFactory.sparqlService(ENDPOINT_SELECT, query);
//        ResultSet rs = qexec.execSelect();
//        String txt = ResultSetFormatter.asXMLString(rs);
//
//        System.out.println(SparqlUtil.unEscapeText(txt));
    }

    private static void insertTriplesIntoDataset() throws IOException {
        DatasetAccessor datasetAccessor = DatasetAccessorFactory.createHTTP("http://localhost:3030/myapp2/data");
        Model model = datasetAccessor.getModel(GRAPH_NAME);

        if(model == null) {
            model = ModelFactory.createDefaultModel();
            datasetAccessor.putModel(GRAPH_NAME,model);
        }

        for(int i=0;i<15000;i++){
            String s = "http://example.org/sample/s" + i;
            String p = "urn:my:summary";

            InputStream is = SparqlTester.class.getClassLoader().getResourceAsStream("sample.txt");
            String o = IOUtils.toString(is);

            Statement stmt = createStatement(s, p, o);
            model.remove(stmt);
            model.add(stmt);

            p = "urn:my:id";
            stmt = createStatement(s, p, i);
            model.add(stmt);

            p = "urn:my:mailTo";
            URI uri = null;
            if (i % 2 == 0) {
                uri = new URI("http://example.org/sample/jake%40abc.com");
                stmt = createStatement(uri.getUri(), "urn:my:email", "jake@abc.com");
            } else {
                uri = new URI("http://example.org/sample/fitz%40abc.com");
                stmt = createStatement(uri.getUri(), "urn:my:email", "fitz@abc.com");
            }
            model.add(stmt);

            stmt = createStatement(s, p, uri);
            model.add(stmt);
        }

        datasetAccessor.putModel(GRAPH_NAME,model);
    }

    private static Statement createStatement(String s, String p, Object o) {
        Statement stmt;
        if (o instanceof URI) {
            URI uri = (URI) o;
            stmt = ResourceFactory.createStatement(ResourceFactory.createResource(s),
                    ResourceFactory.createProperty(p), ResourceFactory.createResource(uri.getUri()));

        } else {
            stmt = ResourceFactory.createStatement(ResourceFactory.createResource(s),
                    ResourceFactory.createProperty(p), ResourceFactory.createTypedLiteral(o));
        }

        return stmt;
    }

    private static void deleteTriples() {
        UpdateRequest updateRequest = UpdateFactory.create();
        for (int i = 0; i < 15000; i++) {

            DeleteQueryBuilder deleteQueryBuilder = new DeleteQueryBuilder(GRAPH_NAME);
            deleteQueryBuilder.appendDeleteBySubjectPredicate("http://example.org/sample/s"+i, "urn:my:summary");

            updateRequest.add(deleteQueryBuilder.toQuery());
        }
        UpdateProcessor processor = UpdateExecutionFactory.createRemote(updateRequest, ENDPOINT_UPDATE);
        processor.execute();
    }

    private static void insertTriples() throws IOException {
        UpdateRequest updateRequest = UpdateFactory.create();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for(int i=0;i<15000;i++) {
            InsertQueryBuilder insertQueryBuilder = new InsertQueryBuilder("http://example.org/SM_GRAPH");
            String s = "http://example.org/sample/s" + i;
            String p = "urn:my:summary";
            InputStream is = SparqlTester.class.getClassLoader().getResourceAsStream("sample.txt");

            String o = IOUtils.toString(is);
//        String o = "hello";
            insertQueryBuilder.appendSparqlTriple(s, p, o);

            p = "urn:my:id";
            insertQueryBuilder.appendSparqlTriple(s, p, i);

            p = "urn:my:mailTo";
            URI uri = null;
            if (i % 2 == 0) {
                uri = new URI("http://example.org/sample/jake%40abc.com");
                insertQueryBuilder.appendSparqlTriple(uri.getUri(), "urn:my:email", "jake@abc.com");
//                insertQueryBuilder.appendReifiedTriple(uri.getUri(), "urn:my:email", "jake@abc.com", "urn:my:role", "role_1", "role_2", "role_3");

            } else {
                uri = new URI("http://example.org/sample/fitz%40abc.com");
                insertQueryBuilder.appendSparqlTriple(uri.getUri(), "urn:my:email", "fitz@abc.com");
//                insertQueryBuilder.appendReifiedTriple(uri.getUri(), "urn:my:email", "fitz@abc.com", "urn:my:role", "role_1", "role_2", "role_3");
            }
            insertQueryBuilder.appendSparqlTriple(s, p, uri);
            updateRequest.add(insertQueryBuilder.toQuery());
        }

        UpdateProcessor processor = UpdateExecutionFactory.createRemote(updateRequest, ENDPOINT_UPDATE);
        processor.execute();

        stopWatch.stop();
        System.out.println(stopWatch.toString());
    }
}
