package com.gaelmarhic.quadrant.models

import com.gaelmarhic.quadrant.models.Constants.ACTIVITY_TAG
import com.gaelmarhic.quadrant.models.Constants.APPLICATION_TAG
import javax.xml.bind.annotation.XmlAccessType.FIELD
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = APPLICATION_TAG)
@XmlAccessorType(FIELD)
data class Application(
    @field:XmlElement(name = ACTIVITY_TAG)
    val activities: MutableList<Activity>
) {
    @Suppress("unused")
    constructor() : this(activities = mutableListOf())
}
