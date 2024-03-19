package com.saveourtool.template.authentication

import com.fasterxml.jackson.annotation.JsonIgnore
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.springframework.http.HttpHeaders
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken

class AppUserDetails(
    val id: Long,
    val name: String,
    val role: String,
): UserDetails {

    /**
     * @return [PreAuthenticatedAuthenticationToken]
     */
    fun toPreAuthenticatedAuthenticationToken() =
        PreAuthenticatedAuthenticationToken(this, null, authorities)

    /**
     * Populates `X-Authorization-*` headers
     *
     * @param httpHeaders
     */
    fun populateHeaders(httpHeaders: HttpHeaders) {
        httpHeaders.set(AUTHORIZATION_ID, id.toString())
        httpHeaders.set(AUTHORIZATION_NAME, name)
        httpHeaders.set(AUTHORIZATION_ROLES, role)
    }

    @JsonIgnore
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = AuthorityUtils.commaSeparatedStringToAuthorityList(role)

    @JsonIgnore
    override fun getPassword(): String? = null

    @JsonIgnore
    override fun getUsername(): String = name

    @JsonIgnore
    override fun isAccountNonExpired(): Boolean = true

    @JsonIgnore
    override fun isAccountNonLocked(): Boolean = true

    @JsonIgnore
    override fun isCredentialsNonExpired(): Boolean = true

    @JsonIgnore
    override fun isEnabled(): Boolean = true

    @Suppress("UastIncorrectHttpHeaderInspection")
    companion object {
        @Suppress("GENERIC_VARIABLE_WRONG_DECLARATION")
        private val log = logger {}

        /**
         * `X-Authorization-Roles` used to specify application's user id
         */
        const val AUTHORIZATION_ID = "X-Authorization-Id"

        /**
         * `X-Authorization-Roles` used to specify application's username
         */
        const val AUTHORIZATION_NAME = "X-Authorization-Name"

        /**
         * `X-Authorization-Roles` used to specify application's user roles
         */
        const val AUTHORIZATION_ROLES = "X-Authorization-Roles"

        /**
         * An attribute to store application's user
         */
        const val APPLICATION_USER_ATTRIBUTE = "application-user"

        /**
         * @return [AppUserDetails] created from values in headers
         */
        fun HttpHeaders.toAppUserDetails(): AppUserDetails? {
            return AppUserDetails(
                id = getSingleHeader(AUTHORIZATION_ID)?.toLong() ?: return logWarnAndReturnEmpty(AUTHORIZATION_ID),
                name = getSingleHeader(AUTHORIZATION_NAME) ?: return logWarnAndReturnEmpty(AUTHORIZATION_NAME),
                role = getSingleHeader(AUTHORIZATION_ROLES) ?: return logWarnAndReturnEmpty(AUTHORIZATION_ROLES),
            )
        }

        private fun HttpHeaders.getSingleHeader(headerName: String) = get(headerName)?.singleOrNull()

        private fun <T> logWarnAndReturnEmpty(missedHeaderName: String): T? {
            log.debug {
                "Header $missedHeaderName is not provided: skipping pre-authenticated save-user authentication"
            }
            return null
        }
    }
}
