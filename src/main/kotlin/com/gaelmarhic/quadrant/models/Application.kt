package com.gaelmarhic.quadrant.models

import com.gaelmarhic.quadrant.models.Constants.ACTIVITY_TAG
import com.gaelmarhic.quadrant.models.Constants.APPLICATION_TAG
import com.gaelmarhic.quadrant.models.Constants.METADATA_TAG
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
