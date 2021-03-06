/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.boot.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.IPagingProvider;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;

import java.util.Optional;

/**
 * @author Christian Ohr
 */
public class IpfBootFhirServlet extends IpfFhirServlet {

    private final Optional<IPagingProvider> pagingProvider;

    public IpfBootFhirServlet(FhirContext fhirContext, Optional<IPagingProvider> pagingProvider) {
        super(fhirContext);
        this.pagingProvider = pagingProvider;
    }

    @Override
    protected IPagingProvider getDefaultPagingProvider(int pagingProviderSize) {
        return pagingProvider.orElse(super.getDefaultPagingProvider(pagingProviderSize));
    }
}
