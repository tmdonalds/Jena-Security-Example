package com.security.examples;

import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.rdf.model.*;

/**
 * Created by trevor on 1/28/15.
 */
public class FusekiExample {

    public static final String GRAPH_NAME = "http://example.org/ModelA";
    public static final String DS_URL = "http://localhost:8080/fuseki/myDataset/data";

    public static void main(String[] args) {
        DatasetAccessor datasetAccessor = DatasetAccessorFactory.createHTTP(DS_URL);
        System.out.println(datasetAccessor.containsModel(GRAPH_NAME));
        Model model;
        model = getModel(datasetAccessor);

//        model.removeAll();
//        Statement newStmt = fetchStatement(model,datasetAccessor);

//        model.remove(newStmt);


        listStatements(model);


//        model.write(System.out, "TTL");
        model.close();
    }

    private static void listStatements(Model model) {
        StmtIterator stmtIterator = model.listStatements();
        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.next();
            if (model.isReified(stmt)) {
                System.out.println("Statement is reified.");
                System.out.println(stmt);
            }
        }
    }

    private static Statement fetchStatement(Model model, DatasetAccessor datasetAccessor) {
        Resource subject = ResourceFactory.createResource("http://myapp/tasker/1");
        Property predicate = ResourceFactory.createProperty("urn:ma:id");
        RDFNode objectNode = ResourceFactory.createTypedLiteral("1");
        Statement newStmt = model.createStatement(subject, predicate, objectNode);

        ReifiedStatement rs = newStmt.createReifiedStatement();
        Property maRoles = ResourceFactory.createProperty("urn:ma:roles");
        String[] roles = new String[]{"ROLE_A", "ROLE_B"};
        for (String role : roles) {
            rs.addProperty(maRoles, role);
        }

        model.add(newStmt);
        datasetAccessor.add(GRAPH_NAME, model);

        return newStmt;
    }

    private static Model getModel(DatasetAccessor datasetAccessor) {
        Model model;
        if (datasetAccessor.containsModel(GRAPH_NAME)) {
            model = datasetAccessor.getModel(GRAPH_NAME);
        } else {
            model = ModelFactory.createDefaultModel();
        }
        return model;
    }
}
