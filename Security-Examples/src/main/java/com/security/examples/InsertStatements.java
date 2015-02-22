package com.security.examples;

import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by trevor on 1/23/15.
 */
public class InsertStatements {
    public static void main(String[] args) throws IOException {

//        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("InsertStatements.spql");
//        String queryString = IOUtils.toString(is, "UTF-8");
        File file = new File("output/temp.spql");

        String queryString = FileUtils.readFileToString(file);
        UpdateRequest ur = UpdateFactory.create(queryString);
        UpdateProcessor processor = UpdateExecutionFactory.createRemote(ur, "http://localhost:8080/fuseki/securedDataset/update");
        processor.execute();
        System.out.println("UPDATED SUCCESSFULLY!");
    }
}
