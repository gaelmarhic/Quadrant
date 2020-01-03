package com.gaelmarhic.quadrant.models

import com.gaelmarhic.quadrant.constants.ModelConstants.ANDROID_NAMESPACE
import com.gaelmarhic.quadrant.constants.ModelConstants.METADATA_NAME_ATTRIBUTE
import com.gaelmarhic.quadrant.constants.ModelConstants.METADATA_TAG
import com.gaelmarhic.quadrant.constants.ModelConstants.METADATA_VALUE_ATTRIBUTE
import javax.xml.bind.annotation.XmlAccessType.FIELD
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = METADATA_TAG)
@XmlAccessorType(FIELD)
data class MetaData(
    @field:XmlAttribute(name = METADATA_NAME_ATTRIBUTE, namespace = ANDROID_NAMESPACE)
    val name: String,
    @field:XmlAttribute(name = METADATA_VALUE_ATTRIBUTE, namespace = ANDROID_NAMESPACE)
    val value: String
) {
    @Suppress("unused")
    constructor() : this(name = "", value = "")
}
