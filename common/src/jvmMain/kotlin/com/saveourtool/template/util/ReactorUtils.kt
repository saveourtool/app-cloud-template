/**
 * Utility methods for working with Reactor publishers
 */

package com.saveourtool.template.util

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

/**
 * @param status
 * @param messageCreator
 * @return original [Mono] or [Mono.error] with [status] otherwise
 */
fun <T> Mono<T>.switchIfEmptyToResponseException(status: HttpStatus, messageCreator: (() -> String?) = { null }) = switchIfEmpty {
    Mono.error(ResponseStatusException(status, messageCreator()))
}
