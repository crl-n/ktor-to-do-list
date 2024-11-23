package com.carlnysten.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateTaskCollectionDTO(
    val name : String,
    val description : String?
)
