/*
 * #%L
 * Wildfly Camel :: Example :: Camel CXF JAX-RS
 * %%
 * Copyright (C) 2013 - 2016 RedHat
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.wildfly.camel.examples.cxf.jaxrs;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;

@SuppressWarnings("serial")
@WebServlet(name = "HttpServiceServlet", urlPatterns = { "/cxf/*", "/example-camel-cxfrs/cxf/*" }, loadOnStartup = 1)
public class CamelCxfRsServlet extends HttpServlet {

    @Resource(lookup = "java:jboss/camel/context/cxfrs-camel-context")
    private CamelContext camelContext;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        /**
         * Get the name parameter sent on the POST request
         */
        String name = request.getParameter("name");

        /**
         * Create a ProducerTemplate to invoke the direct:start endpoint, which will
         * result in the greeting REST service 'greet' method being invoked.
         *
         * The service parameters are sent to camel as an object array which is
         * set as the request message body.
         *
         * The REST service result string is returned back for display on the UI.
         */
        ProducerTemplate producer = camelContext.createProducerTemplate();
        Object[] serviceParams = new Object[] {name};
        String result = producer.requestBody("direct:start", serviceParams, String.class);

        request.setAttribute("greeting", result);
        request.getServletContext().getRequestDispatcher("/greeting.jsp").forward(request, response);
    }
}
