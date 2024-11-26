package com.carlnysten.enum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TaskPriority {
    @SerialName("low") Low,
    @SerialName("normal") Normal,
    @SerialName("high") High
}
