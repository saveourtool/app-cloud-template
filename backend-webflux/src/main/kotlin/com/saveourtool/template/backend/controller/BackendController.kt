package com.saveourtool.template.backend.controller

import com.saveourtool.template.backend.model.Request
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux

/**
 * @since 2024-02-26
 */
@RestController
@RequestMapping("/requests")
class BackendController {
    private val requests = mutableListOf<Request>()

    /**
     * @param request a new [Request]
     */
    @PostMapping("/new")
    fun newEntity(
        @RequestBody request: Mono<Request>,
    ): Mono<Unit> = request.map { requests.add(it) }

    /**
     * @return all [Request]s
     */
    @GetMapping("/all")
    fun getAllEntities(): Flux<Request> = requests.toFlux()
}