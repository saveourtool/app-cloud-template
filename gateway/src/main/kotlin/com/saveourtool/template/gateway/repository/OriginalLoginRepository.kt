package com.saveourtool.template.gateway.repository

import com.saveourtool.template.gateway.entities.OriginalLogin
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository to access data about original user logins and sources
 */
@Repository
interface OriginalLoginRepository : JpaRepository<OriginalLogin, Long>{
    /**
     * @param name
     * @param source
     * @return user or null if no results have been found
     */
    fun findByNameAndSource(name: String, source: String): OriginalLogin?

    /**
     * @param id id of user
     */
    fun deleteByUserId(id: Long)
}
