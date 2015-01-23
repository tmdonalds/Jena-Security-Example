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
package org.apache.jena.fuseki.shiro.web.filter.authc.x509;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.security.cert.X509Certificate;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationToken;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("PackageAccessibility")
public class X509AuthenticationFilterTest {

    private X509Certificate certificate;

    @Before
    public void setUp() {
        certificate = mock(X509Certificate.class);
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("CN=Test, L=London, C=GB");
        when(certificate.getSubjectDN()).thenReturn(principal);
    }

    @Test
    public void onLoginFailure() {
        X509AuthenticationFilter filter = new X509AuthenticationFilter();
        assertFalse(filter.onLoginFailure(null, null, null, null));
    }

//    @Test(expected = ShiroException.class)
//    public void createTokenWithoutCertificate() throws Exception {
//        X509AuthenticationFilter filter = new X509AuthenticationFilter();
//        ServletRequest request = mock(ServletRequest.class);
//        ServletResponse response = mock(ServletResponse.class);
//        filter.createToken(request, response);
//    }

    @Test
    public void createTokenWithOneCertificate() throws Exception {
        X509AuthenticationFilter filter = new X509AuthenticationFilter();
        ServletRequest request = mock(ServletRequest.class);
        when(request.getAttribute("javax.servlet.request.X509Certificate")).thenReturn(new X509Certificate [] { certificate });
        ServletResponse response = mock(ServletResponse.class);

        AuthenticationToken token = filter.createToken(request, response);
        assertNotNull(token);
        assertEquals("testUser", token.getPrincipal());
    }

    @Test
    public void createTokenWithManyOneCertificates() throws Exception {
        X509AuthenticationFilter filter = new X509AuthenticationFilter();
        ServletRequest request = mock(ServletRequest.class);
        when(request.getAttribute("javax.servlet.request.X509Certificate")).thenReturn(new X509Certificate [] { certificate, mock(X509Certificate.class) });
        ServletResponse response = mock(ServletResponse.class);

        AuthenticationToken token = filter.createToken(request, response);
        assertNotNull(token);
        assertEquals("testUser", token.getPrincipal());
    }

}