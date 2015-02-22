package com.security.examples.queryBuilder;

/**
 * Created by trevor on 2/21/15.
 */
public abstract class AbstractQueryBuilder {
    public static final String SPACE = " ";
    protected StringBuilder cmd;
    protected StringBuilder footer;

    protected String SUFFIX_PERIOD="";
    protected String LINE_SEPARATOR="";
    protected String APPENDER_SEMICOLON="";

    protected void addSuffixes() {
        SUFFIX_PERIOD = " .";
        LINE_SEPARATOR = System.lineSeparator();
        APPENDER_SEMICOLON = " ;";
    }

    /**
     * adds a line with ; and \n\n
     */
    protected void addAppenderLine() {
        cmd.append(APPENDER_SEMICOLON);
        cmd.append(LINE_SEPARATOR);
    }

    /**
     * adds a line with . and \n\n
     */
    protected void addDoubleLine() {
        cmd.append(SUFFIX_PERIOD);
        cmd.append(LINE_SEPARATOR);
        cmd.append(LINE_SEPARATOR);
    }

    /**
     * adds a line with . and \n\n
     */
    protected void addSingleLine() {
        cmd.append(SUFFIX_PERIOD);
        cmd.append(LINE_SEPARATOR);
    }


    @Override
    public String toString() {
        return cmd.toString();
    }

    public abstract String toQuery();
}
