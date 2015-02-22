package com.security.examples;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by trevor on 1/31/15.
 */
public class TempStatementBuilder {
    public static final String HTTP_EXAMPLE_ORG = "http://example.org";

    private static List<String> roleList = new ArrayList<>();

    static {
        roleList.add("\"ROLE_A\"");
        roleList.add("\"ROLE_B\"");
        roleList.add("\"ROLE_C\"");
        roleList.add("\"ROLE_D\"");
        roleList.add("\"ROLE_E\"");
        roleList.add("\"ROLE_F\"");
        roleList.add("\"ROLE_G\"");
        roleList.add("\"ROLE_H\"");
        roleList.add("\"ROLE_I\"");
    }

    public static void main(String[] args) throws IOException {
        File file = new File("output/temp.spql");
        FileUtils.writeStringToFile(file,fetchInsertStatements());
        System.out.println("completed");
    }

    public static String fetchInsertStatements() throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("TempStatementBuilder.spql");
        String tempString = IOUtils.toString(is, "UTF-8");

        String statements = fetchStatements();
        String sparqlQuery = tempString.replaceFirst("TEMP_HOLDING", statements);

        return sparqlQuery;
    }

    private static String fetchStatements() throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();

        int id = 0;
        for(int idx = 0; idx < 100; idx++) {
            id++;
            String taskIdUri = String.format("<" + HTTP_EXAMPLE_ORG + "/task/%s>", id);
            String taskIdStatement = String.format("%s  ma:id  \"%s\" .\n", taskIdUri, id);

            String emailusername = RandomStringUtils.random(8, true, false);
            String emailEncoded = String.format("%s@abc.com", emailusername);
            emailEncoded = URLEncoder.encode(emailEncoded, "UTF-8");
            String emailUri = String.format("<" + HTTP_EXAMPLE_ORG + "/task/email/%s>", emailEncoded);
            String emailTemplate = String.format("%s ma:value \"%s@abc.com\" .\n", emailUri, emailusername);
            String hasEmailTemplate = String.format("%s  ma:hasEmail  %s .\n", taskIdUri, emailUri);

            String name = RandomStringUtils.random(8, true, false);
            String nameStatement = String.format("%s  ma:name  \"%s\" .\n", taskIdUri, name);

            String reifiedIdStatement = " []    a    <https://www.w3.org/1999/02/22-rdf-syntax-ns#Statement> ;\n" +
                    "      <http://www.w3.org/1999/02/22-rdf-syntax-ns#object>\n" +
                    "      \"%s\" ;\n" +
                    "      <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate>\n" +
                    "      ma:id ;\n" +
                    "      <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject>\n" +
                    "      %s ;\n" +
                    "      <urn:myApp:role>%s\n" +
                    "      .\n";
            int max = 9;
            int min = 1;
            Random random = new Random();
            int num = random.nextInt(max - min + 1) + min;

            List<String> subList = getRandomSubList(roleList, num);
            StringBuilder rb = new StringBuilder();
            String prefix = "";
            for (String s : subList) {
                rb.append(prefix);
                rb.append(s);
                prefix = ",";
            }

            reifiedIdStatement = String.format(reifiedIdStatement, id, taskIdUri, rb.toString());
            sb.append(taskIdStatement);
            sb.append(emailTemplate);
            sb.append(hasEmailTemplate);
            sb.append(nameStatement);
            sb.append(reifiedIdStatement);

        }
        return sb.toString();
    }

    public static <T> List<T> getRandomSubList(List<T> input, int subsetSize)
    {
        Random r = new Random();
        int inputSize = input.size();
        for (int i = 0; i < subsetSize; i++)
        {
            int indexToSwap = i + r.nextInt(inputSize - i);
            T temp = input.get(i);
            input.set(i, input.get(indexToSwap));
            input.set(indexToSwap, temp);
        }
        return input.subList(0, subsetSize);
    }


}
