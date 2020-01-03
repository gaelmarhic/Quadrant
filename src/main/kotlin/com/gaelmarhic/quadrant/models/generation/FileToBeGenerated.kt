package com.gaelmarhic.quadrant.models.generation

data class FileToBeGenerated(
    val name: String,
    val constantList: List<ConstantToBeGenerated>
)
