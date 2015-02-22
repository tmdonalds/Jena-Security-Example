package com.security.examples.queryBuilder;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Created by trevor on 2/21/15.
 */
public class SparqlUtil {
    public static String unEscapeText(String text) {
        String t = StringEscapeUtils.unescapeJava(text);
        return t;
    }

    public static String createUri(String uri) {
        if(uri.contains("<") || uri.contains(">")) {
            throw new IllegalArgumentException("URI can not contain < or >");
        }

        return "<"+uri+">";
    }
}
