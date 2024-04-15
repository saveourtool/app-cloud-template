/**
 * Utility methods for working with Reactor publishers
 */

package com.saveourtool.template.util

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono

private val ioScheduler: Scheduler = Schedulers.boundedElastic()

/**
 * @param status
 * @param messageCreator
 * @return original [Mono] or [Mono.error] with [status] otherwise
 */
fun <T> Mono<T>.switchIfEmptyToResponseException(status: HttpStatus, messageCreator: (() -> String?) = { null }) = switchIfEmpty {
    Mono.error(ResponseStatusException(status, messageCreator()))
}


/**
 * Taking from https://projectreactor.io/docs/core/release/reference/#faq.wrap-blocking
 *
 * @param supplier blocking operation like JDBC
 * @return [Mono] from result of blocking operation [T]
 * @see blockingToFlux
 */
fun <T : Any> blockingToMono(supplier: () -> T?): Mono<T> = supplier.toMono().subscribeOn(ioScheduler)

/**
 * @param supplier blocking operation like JDBC
 * @return [Flux] from result of blocking operation [List] of [T]
 * @see blockingToMono
 */
fun <T> blockingToFlux(supplier: () -> Iterable<T>): Flux<T> = blockingToMono(supplier).flatMapIterable { it }
