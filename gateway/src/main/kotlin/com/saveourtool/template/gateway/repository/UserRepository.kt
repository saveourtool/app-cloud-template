package com.saveourtool.template.gateway.repository

import com.saveourtool.template.gateway.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository to access data about users
 */
@Repository
interface UserRepository : JpaRepository<User, Long> {
    /**
     * @param username
     * @return user or null if no results have been found
     */
    fun findByName(username: String): User?
}
