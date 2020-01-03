package com.gaelmarhic.quadrant.helpers

import com.gaelmarhic.quadrant.models.manifest.Manifest
import com.gaelmarhic.quadrant.models.modules.ParsedModule
import com.gaelmarhic.quadrant.models.modules.RawModule
import java.io.File
import javax.xml.bind.JAXBContext

class ManifestParsingHelper {

    private val jaxbUnMarshaller by lazy { JAXBContext.newInstance(Manifest::class.java).createUnmarshaller() }

    fun parse(rawModules: List<RawModule>) =
        rawModules
            .map { it.parse() }

    private fun RawModule.parse() = ParsedModule(
        name = name,
        manifestList = manifestFiles.toManifestList()
    )

    private fun List<File>.toManifestList() = this
        .map { jaxbUnMarshaller.unmarshal(it) }
        .map { it as Manifest }
}
