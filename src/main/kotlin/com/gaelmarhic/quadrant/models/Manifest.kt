package com.gaelmarhic.quadrant.models

import com.gaelmarhic.quadrant.constants.ModelConstants.APPLICATION_TAG
import com.gaelmarhic.quadrant.constants.ModelConstants.MANIFEST_TAG
import javax.xml.bind.annotation.XmlAccessType.FIELD
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = MANIFEST_TAG)
@XmlAccessorType(FIELD)
data class Manifest(
    @field:XmlElement(name = APPLICATION_TAG)
    val application: Application
) {
    @Suppress("unused")
    constructor() : this(application = Application(activityList = mutableListOf(), metaDataList = mutableListOf()))
}
