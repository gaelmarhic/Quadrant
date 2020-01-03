package com.gaelmarhic.quadrant.models

import com.gaelmarhic.quadrant.constants.ModelConstants.ACTIVITY_NAME_ATTRIBUTE
import com.gaelmarhic.quadrant.constants.ModelConstants.ACTIVITY_TAG
import com.gaelmarhic.quadrant.constants.ModelConstants.ANDROID_NAMESPACE
import com.gaelmarhic.quadrant.constants.ModelConstants.METADATA_TAG
import javax.xml.bind.annotation.XmlAccessType.FIELD
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = ACTIVITY_TAG)
@XmlAccessorType(FIELD)
data class Activity(
    @field:XmlAttribute(name = ACTIVITY_NAME_ATTRIBUTE, namespace = ANDROID_NAMESPACE)
    val name: String,
    @field:XmlElement(name = METADATA_TAG)
    val metaDataList: MutableList<MetaData>
) {
    @Suppress("unused")
    constructor() : this(name = "", metaDataList = mutableListOf())
}
