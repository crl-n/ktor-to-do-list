package com.carlnysten.enum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TaskStatus {
    @SerialName("pending") Pending,
    @SerialName("done") Done,
}
