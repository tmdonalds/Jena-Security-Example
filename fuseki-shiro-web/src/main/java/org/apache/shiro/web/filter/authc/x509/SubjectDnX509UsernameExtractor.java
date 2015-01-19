/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.shiro.web.filter.authc.x509;

import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.shiro.ShiroException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Obtains the principal from a certificate using a regular expression match against the Subject (as returned by a call
 * to {@link X509Certificate#getSubjectDN()}).
 * <p>
 * The regular expression should contain a single group; for example the default expression "CN=(.*?)(?:,|$)" matches the
 * common name field. So "CN=Jimi Hendrix, OU=..." will give a user name of "Jimi Hendrix".
 * <p>
 * The matches are case insensitive. So "emailAddress=(.?)," will match "EMAILADDRESS=jimi@hendrix.org, CN=..." giving a
 * user name "jimi@hendrix.org"
 */
public class SubjectDnX509UsernameExtractor implements X509UsernameExtractor {

    private static final Logger log = LoggerFactory.getLogger(SubjectDnX509UsernameExtractor.class);

    private Pattern subjectDnPattern;

    public SubjectDnX509UsernameExtractor() {
        setSubjectDnRegex("CN=(.*?)(?:,|$)");
    }

    /**
     * Sets the regular expression which will by used to extract the user name from the certificate's Subject
     * DN.
     * <p>
     * It should contain a single group; for example the default expression "CN=(.*?)(?:,|$)" matches the common
     * name field. So "CN=Jimi Hendrix, OU=..." will give a user name of "Jimi Hendrix".
     * <p>
     * The matches are case insensitive. So "emailAddress=(.?)," will match "EMAILADDRESS=jimi@hendrix.org,
     * CN=..." giving a user name "jimi@hendrix.org"
     *
     * @param subjectDnRegex the regular expression to find in the subject
     */
    public void setSubjectDnRegex(String subjectDnRegex) {
        if (subjectDnRegex == null || subjectDnRegex.length() == 0) {
            throw new IllegalArgumentException("Regular expression for subjectDnRegex may not be null or empty");
        }

        subjectDnPattern = Pattern.compile(subjectDnRegex, Pattern.CASE_INSENSITIVE);
    }

    @Override
    public String extractUsername(final X509Certificate certificate) {
        //String subjectDN = certificate.getSubjectX500Principal().getName();
        String subjectDN = certificate.getSubjectDN().getName();

        log.debug(String.format("Subject DN is '%s'", subjectDN));

        Matcher matcher = subjectDnPattern.matcher(subjectDN);

        if (!matcher.find()) {
            throw new ShiroException(String.format("No matching pattern was found in subject DN: %s", subjectDN));
        }

        if (matcher.groupCount() != 1) {
            throw new IllegalArgumentException("Regular expression must contain a single group.");
        }

        String username = matcher.group(1);

        log.debug("Extracted Principal name is '" + username + "'");

        return username;
    }

}