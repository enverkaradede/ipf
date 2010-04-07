/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.core.validate.responses;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLQueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.EbXMLFactory21;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.transform.responses.QueryResponseTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

/**
 * Test for {@link QueryResponseValidator}.
 * @author Jens Riemschneider
 */
public class QueryResponseValidatorTest {
    private QueryResponseValidator validator;
    private QueryResponse response;
    private QueryResponseTransformer transformer;
    private DocumentEntry docEntry;

    @Before
    public void setUp() {
        validator = new QueryResponseValidator();
        EbXMLFactory factory = new EbXMLFactory21();
        
        response = SampleData.createQueryResponseWithLeafClass();
        transformer = new QueryResponseTransformer(factory);

        docEntry = response.getDocumentEntries().get(0);
    }
    
    @Test
    public void testValidateGoodCase() {
        validator.validate(transformer.toEbXML(response), null);
    }

    @Test
    public void testQueryResponseDoesNotHaveSubmissionSetLimitations() {
        response.getSubmissionSets().clear();
        validator.validate(transformer.toEbXML(response), null);
    }
    
    @Test
    public void testQueryResponseMultiplePatientIdsDueToDocEntry() {
        Identifiable otherId = new Identifiable("idbla", new AssigningAuthority("1.6"));
        DocumentEntry docEntryOtherPatientId = SampleData.createDocumentEntry(otherId);
        response.getDocumentEntries().add(docEntryOtherPatientId);
        expectFailure(RESULT_NOT_SINGLE_PATIENT);
    }

    @Test
    public void testQueryResponseMultiplePatientIdsDueToFolder() {
        Identifiable otherId = new Identifiable("idbla", new AssigningAuthority("1.6"));
        Folder folderOtherPatientId = SampleData.createFolder(otherId);
        response.getFolders().add(folderOtherPatientId);
        expectFailure(RESULT_NOT_SINGLE_PATIENT);
    }

    @Test
    public void testQueryResponseMultiplePatientIdsDueToSubmissionSet() {
        Identifiable otherId = new Identifiable("idbla", new AssigningAuthority("1.6"));
        SubmissionSet submissionSetOtherPatientId = SampleData.createSubmissionSet(otherId);
        response.getSubmissionSets().add(submissionSetOtherPatientId);
        expectFailure(RESULT_NOT_SINGLE_PATIENT);
    }

    @Test
    public void testValidateDelegatesToSubmitObjectsRequestValidator() {
        // Try a failure that is produced by the SubmitObjectsRequestValidator
        docEntry.getAuthors().get(0).getAuthorInstitution().add(new Organization(null, "LOL", null));
        expectFailure(ORGANIZATION_NAME_MISSING);            
    }
    
    @Test
    public void testValidateDelegatesToRegistryResponseValidator() {
        // Try a failure that is produced by the RegistryResponseValidator
        response.setStatus(null);
        expectFailure(INVALID_STATUS_IN_RESPONSE);
    }
    
    @Test
    public void testMissingObjRef() {
        response.getReferences().add(new ObjectReference());        
        expectFailure(MISSING_OBJ_REF);
    }
    
    private void expectFailure(ValidationMessage expectedMessage) {
        expectFailure(expectedMessage, transformer.toEbXML(response));
    }

    private void expectFailure(ValidationMessage expectedMessage, EbXMLQueryResponse ebXMLQueryResponse) {
        try {
            validator.validate(ebXMLQueryResponse, null);
            fail("Expected exception: " + XDSMetaDataException.class);
        }
        catch (XDSMetaDataException e) {
            assertEquals(expectedMessage, e.getValidationMessage());
        }
    }
}