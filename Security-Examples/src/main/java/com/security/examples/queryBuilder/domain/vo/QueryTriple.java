package com.security.examples.queryBuilder.domain.vo;

/**
 * Created by trevor on 2/21/15.
 */
public class QueryTriple {
    private final String subject;
    private final String predicate;
    private final String object;

    public QueryTriple(Builder built) {
        this.subject = built.subject;
        this.predicate = built.predicate;
        this.object = built.object;
    }

    public String getSubject() {
        return subject;
    }

    public String getPredicate() {
        return predicate;
    }

    public String getObject() {
        return object;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder{

        private String subject;
        private String predicate;
        private String object;

        public Builder withSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder withPredicate(String predicate) {
            this.predicate = predicate;
            return this;
        }

        public Builder withObject(String object) {
            this.object = object;
            return this;
        }

        public QueryTriple build() {
            return new QueryTriple(this);
        }
    }
}
