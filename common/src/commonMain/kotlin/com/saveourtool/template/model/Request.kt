package com.saveourtool.template.model

import kotlinx.serialization.Serializable

/**
 * @since 2024-03-11
 */
@Serializable
data class Request(
    val id: Long,
    val name: String,
    val operation: String,
)
