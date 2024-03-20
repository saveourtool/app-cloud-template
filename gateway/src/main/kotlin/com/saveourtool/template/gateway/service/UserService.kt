package com.saveourtool.template.gateway.service

import com.saveourtool.template.gateway.entities.OriginalLogin
import com.saveourtool.template.gateway.entities.Role
import com.saveourtool.template.gateway.entities.User
import com.saveourtool.template.gateway.repository.OriginalLoginRepository
import com.saveourtool.template.gateway.repository.UserRepository
import org.springframework.transaction.annotation.Transactional

/**
 * Service for [User]
 */
open class UserService(
    private val userRepository: UserRepository,
    private val originalLoginRepository: OriginalLoginRepository,
) {
    /**
     * @param username
     * @return existed [User]
     */
    fun findByName(username: String): User? =
        userRepository.findByName(username)

    /**
     * @param source source (where the user identity is coming from)
     * @param nameInSource name provided by source
     * @return existed [User] or a new created [User]
     */
    @Transactional
    open fun getOrCreateNew(
        source: String,
        nameInSource: String,
    ): User = originalLoginRepository.findByNameAndSource(nameInSource, source)
        ?.user
        ?: run {
            val newUser = User(
                name = nameInSource,
                password = null,
                role = Role.VIEWER.formattedName,
            )
                .let { userRepository.save(it) }
            originalLoginRepository.save(
                OriginalLogin(
                    name = nameInSource,
                    source = source,
                    user = newUser
                )
            )
            newUser
        }
}