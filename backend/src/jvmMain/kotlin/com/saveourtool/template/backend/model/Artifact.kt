package com.saveourtool.template.backend.model

/**
 * A model for artifact
 *
 * @since 2024-02-26
 */
data class Artifact(
    val registry: UpstreamRegistry,
    val id: String,
    val version: String,
)
