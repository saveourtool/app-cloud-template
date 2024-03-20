package com.saveourtool.template.gateway.service

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
     * @param source source (where the user identity is coming from)
     * @return existed [User]
     */
    fun findByOriginalLogin(username: String, source: String): User? =
        originalLoginRepository.findByNameAndSource(username, source)?.user

    /**
     * @param source source (where the user identity is coming from)
     * @param nameInSource name provided by source
     * @return existed [User] or a new created [User]
     */
    @Transactional
    open fun getOrCreateNew(
        source: String,
        nameInSource: String,
    ): User {
        originalLoginRepository.findByNameAndSource(nameInSource, source)
            ?.user
            ?: run {
                val newUser = User(

                )
                userRepository.save(newUser)
            }
    }
}