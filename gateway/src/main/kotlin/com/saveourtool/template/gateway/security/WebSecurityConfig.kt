/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2024-2024. All rights reserved.
 */

package com.saveourtool.template.gateway.security

import com.saveourtool.template.gateway.service.UserService
import com.saveourtool.template.util.blockingToMono
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.DelegatingServerAuthenticationSuccessHandler
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationFailureHandler
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler

/**
 * @since 2024-03-20
 */
@EnableWebFluxSecurity
class WebSecurityConfig(
    private val userService: UserService,
) {
    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity
    ): SecurityWebFilterChain = http
        .oauth2Login {
            it.authenticationSuccessHandler(
                DelegatingServerAuthenticationSuccessHandler(
                    StoringServerAuthenticationSuccessHandler(backendService),
                    RedirectServerAuthenticationSuccessHandler("/"),
                )
            )
            it.authenticationFailureHandler(
                RedirectServerAuthenticationFailureHandler("/error")
            )
        }
        .httpBasic { httpBasicSpec ->
            // Authenticate by comparing received basic credentials with existing one from DB
            httpBasicSpec.authenticationManager(
                UserDetailsRepositoryReactiveAuthenticationManager { username ->
                    blockingToMono {
                        userService.findByName(username)
                    }
                        .filter { it.password != null }
                        .map {

                        }

                    backendService.findByName(username).cast<UserDetails>()
                }
            )
        }
        .build()
}