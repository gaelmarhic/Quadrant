package com.gaelmarhic.quadrant.models

import com.gaelmarhic.quadrant.models.Constants.ACTIVITY_NAME_ATTRIBUTE
import com.gaelmarhic.quadrant.models.Constants.ACTIVITY_TAG
import com.gaelmarhic.quadrant.models.Constants.ANDROID_NAMESPACE
import javax.xml.bind.annotation.XmlAccessType.FIELD
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = ACTIVITY_TAG)
@XmlAccessorType(FIELD)
data class Activity(
    @field:XmlAttribute(name = ACTIVITY_NAME_ATTRIBUTE, namespace = ANDROID_NAMESPACE)
    val name: String
) {
    @Suppress("unused")
    constructor() : this(name = "")
}
