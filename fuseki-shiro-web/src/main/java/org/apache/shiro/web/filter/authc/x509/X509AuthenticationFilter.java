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

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Authentication filter for X.509 certificates. Incoming requests are expected to be signed with a
 * {@link X509Certificate}. This is ensured by the webserver if configured properly. The username 
 * is extracted from the certificate using a regular expression. This username can then be used
 * with any other realm.
 *
 * Make sure to use the credentials matcher {@link AllowAllCredentialsMatcher} to disable the password
 * within the realm.
 *
 * @see SubjectDnX509UsernameExtractor
 */
@SuppressWarnings("PackageAccessibility")
public class X509AuthenticationFilter extends AuthenticatingFilter {

    private static final Logger log = LoggerFactory.getLogger(X509AuthenticationFilter.class);

    /**
     * Placeholder for the password to avoid null and thereby indicate that the
     * password is deliberately empty.
     */
    public static final String PASSWORD_PLACEHOLDER = "-NO PASSWORD-";

    /**
     * Extractor to extract the principal from the certificates Subject DN field.
     */
    private X509UsernameExtractor usernameExtractor = new SubjectDnX509UsernameExtractor();

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException exception, ServletRequest request, ServletResponse response) {
        log.warn("X509 login failed.", exception);
        return false;
    }

    @Override
    protected boolean onAccessDenied(final ServletRequest request, final ServletResponse response) throws Exception {
        return executeLogin(request, response);
    }

    @Override
    protected AuthenticationToken createToken(final ServletRequest request, final ServletResponse response) throws Exception {
        final X509Certificate[] clientCertificateChain = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");
        log.debug("X509AuthFilter.createToken() cert chain is {}", clientCertificateChain);
        if (clientCertificateChain == null || clientCertificateChain.length < 1) {
            throw new ShiroException("Request do not contain any X509Certificate");
        }

        if (clientCertificateChain.length > 1) {
            log.warn("Found more then one X509 certificate in request from {}. Using the first certificate only.", getHost(request));
        }

        final X509Certificate clientCertificate = clientCertificateChain[0];
        return new UsernamePasswordToken(usernameExtractor.extractUsername(clientCertificate), PASSWORD_PLACEHOLDER, getHost(request));
    }

    public void setUsernameExtractor(final X509UsernameExtractor usernameExtractor) {
        this.usernameExtractor = usernameExtractor;
    }

}