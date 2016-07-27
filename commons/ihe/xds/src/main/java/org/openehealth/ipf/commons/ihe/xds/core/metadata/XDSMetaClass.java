/*
 * Copyright 2009-2011 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.metadata;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openehealth.ipf.commons.ihe.xds.core.ExtraMetadataHolder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters.ExtraMetadata;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters.ExtraMetadataAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Common base class of all XDS meta data classes.
 * <p>
 * All members of this class are allowed to be <code>null</code>.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IdentifiedObject", propOrder = {
        "homeCommunityId", "entryUuid", "logicalUuid", "version", "uniqueId",
        "patientId", "availabilityStatus", "title", "comments", "limitedMetadata",
        "extraMetadata"})
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
abstract public class XDSMetaClass implements Serializable, ExtraMetadataHolder {
    private static final long serialVersionUID = -1193012076778493996L;
    
    @Getter @Setter private AvailabilityStatus availabilityStatus;
    @Getter @Setter private LocalizedString comments;
    @Getter @Setter private String entryUuid;
    @Getter @Setter private Identifiable patientId;
    @Getter @Setter private LocalizedString title;
    @Getter @Setter private String uniqueId;
    @Getter @Setter private String homeCommunityId;
    @Getter @Setter private String logicalUuid;
    @Getter @Setter private Version version;
    @Getter @Setter private boolean limitedMetadata;

    @XmlJavaTypeAdapter(ExtraMetadataAdapter.class)
    @XmlElement(type = ExtraMetadata.class)
    @Getter @Setter private Map<String, List<String>> extraMetadata;

}