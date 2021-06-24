package com.gaelmarhic.quadrant.helpers

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector
import com.gaelmarhic.quadrant.models.manifest.Manifest
import com.gaelmarhic.quadrant.models.modules.ParsedManifest
import com.gaelmarhic.quadrant.models.modules.ParsedModule
import com.gaelmarhic.quadrant.models.modules.RawModule
import java.io.File

class ManifestParsingHelper {

    private val xmlMapper by lazy { initXmlMapper() }

    fun parse(rawModules: List<RawModule>) =
        rawModules
            .map { it.parse() }

    private fun initXmlMapper() =
        XmlMapper().apply {
            setAnnotationIntrospector(JaxbAnnotationIntrospector(typeFactory))
            configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
        }

    private fun RawModule.parse() = ParsedModule(
        name = name,
        manifestList = manifestFiles.toManifestList()
    )

    private fun List<File>.toManifestList() = this
        .map { it.toManifest() }

    private fun File.toManifest() =
        xmlMapper
            .readValue(this, Manifest::class.java)
            .let {
                ParsedManifest(
                    path = absolutePath,
                    application = it.application,
                    packageName = it.packageName
                )
            }
}
