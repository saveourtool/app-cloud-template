package com.saveourtool.template.gateway.service

import com.saveourtool.template.authentication.AppUserDetails
import com.saveourtool.template.authentication.AppUserDetails.Companion.APPLICATION_USER_ATTRIBUTE
import com.saveourtool.template.util.switchIfEmptyToResponseException
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.WebSession
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono
import java.security.Principal
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * A temporary cache for [AppUserDetails]
 */
@Component
class AppUserDetailsService {
    private val reentrantReadWriteLock = ReentrantReadWriteLock()
    private val idGenerator = AtomicLong()
    private val storage: HashMap<Long, AppUserDetails> = hashMapOf()

    /**
     * Saves a new [AppUserDetails] in DB
     *
     * @param source
     * @param nameInSource
     * @return [Mono] with saved [AppUserDetails]
     */
    fun createNewIfRequired(source: String, nameInSource: String): Mono<AppUserDetails> = reentrantReadWriteLock.write {
        AppUserDetails(
            id = idGenerator.incrementAndGet(),
            name = nameInSource,
            role = "VIEWER",
        )
            .also { storage[it.id] = it }
            .toMono()
    }

    /**
     * Find current user [SaveUserDetails] by [principal].
     *
     * @param principal current user [Principal]
     * @param session current [WebSession]
     * @return current user [SaveUserDetails]
     */
    fun findByPrincipal(principal: Principal, session: WebSession): Mono<AppUserDetails> = when (principal) {
        is OAuth2AuthenticationToken -> session.getAppUserDetails().switchIfEmpty {
            findByOriginalLogin(principal.authorizedClientRegistrationId, principal.name)
        }
        is UsernamePasswordAuthenticationToken -> (principal.principal as? SaveUserDetails)
            .toMono()
            .switchIfEmptyToResponseException(HttpStatus.INTERNAL_SERVER_ERROR) {
                "Unexpected principal type ${principal.principal.javaClass} in ${UsernamePasswordAuthenticationToken::class}"
            }
        else -> Mono.error(BadCredentialsException("Unsupported authentication type: ${principal::class}"))
    }

    /**
     * @param id [AppUserDetails.id]
     * @return cached [AppUserDetails] or null
     */
    fun get(id: Long): AppUserDetails? = reentrantReadWriteLock.read {
        storage[id]
    }

    /**
     * Caches provided [appUserDetails]
     *
     * @param appUserDetails [AppUserDetails]
     */
    fun save(appUserDetails: AppUserDetails): Unit = reentrantReadWriteLock.write {
        storage[appUserDetails.id] = appUserDetails
    }

    private fun WebSession.getAppUserDetails(): Mono<AppUserDetails> = this
        .getAttribute<Long>(APPLICATION_USER_ATTRIBUTE)
        .toMono()
        .switchIfEmptyToResponseException(HttpStatus.INTERNAL_SERVER_ERROR) {
            "Not found attribute $APPLICATION_USER_ATTRIBUTE for ${OAuth2AuthenticationToken::class}"
        }
        .mapNotNull { id ->
            get(id)
        }
}