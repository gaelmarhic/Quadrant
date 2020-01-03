package com.gaelmarhic.quadrant.models.manifest

import com.gaelmarhic.quadrant.constants.ModelConstants.ACTIVITY_TAG
import com.gaelmarhic.quadrant.constants.ModelConstants.APPLICATION_TAG
import com.gaelmarhic.quadrant.constants.ModelConstants.METADATA_TAG
import javax.xml.bind.annotation.XmlAccessType.FIELD
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = APPLICATION_TAG)
@XmlAccessorType(FIELD)
data class Application(
    @field:XmlElement(name = ACTIVITY_TAG)
    val activityList: MutableList<Activity>,
    @field:XmlElement(name = METADATA_TAG)
    val metaDataList: MutableList<MetaData>
) {
    @Suppress("unused")
    constructor() : this(activityList = mutableListOf(), metaDataList = mutableListOf())
}
