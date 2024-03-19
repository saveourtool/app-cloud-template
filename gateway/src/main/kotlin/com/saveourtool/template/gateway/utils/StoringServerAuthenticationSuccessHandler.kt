package com.saveourtool.template.gateway.utils

import com.saveourtool.template.authentication.AppUserDetails.Companion.APPLICATION_USER_ATTRIBUTE
import com.saveourtool.template.gateway.service.AppUserDetailsService

import org.slf4j.LoggerFactory
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import reactor.core.publisher.Mono

/**
 * [ServerAuthenticationSuccessHandler] that saves user data in database on successful login
 */
class StoringServerAuthenticationSuccessHandler(
    private val appUserDetailsService: AppUserDetailsService,
) : ServerAuthenticationSuccessHandler {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun onAuthenticationSuccess(
        webFilterExchange: WebFilterExchange,
        authentication: Authentication
    ): Mono<Void> {
        logger.info("Authenticated user ${authentication.name} with authentication type ${authentication::class}, will send data to backend")

        val (source, nameInSource) = if (authentication is OAuth2AuthenticationToken) {
            authentication.authorizedClientRegistrationId to authentication.principal.name
        } else {
            throw BadCredentialsException("Not supported authentication type ${authentication::class}")
        }
        return appUserDetailsService.createNewIfRequired(source, nameInSource).flatMap { appUser ->
            webFilterExchange.exchange.session.map {
                it.attributes[APPLICATION_USER_ATTRIBUTE] = appUser
            }
        }.then()
    }
}
