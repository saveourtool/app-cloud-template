package com.saveourtool.template.backend

import com.saveourtool.template.backend.model.Artifact
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping

/**
 * @since 2024-02-26
 */
@Controller
class BackendController {
    /**
     * A new [Artifact] is detected
     *
     * @param artifact
     */
    @PostMapping("/artifact/new")
    fun newArtifact(artifact: Artifact) {

    }
}