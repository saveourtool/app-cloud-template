package com.saveourtool.template.gateway.service

import com.saveourtool.template.authentication.AppUserDetails
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * A temporary cache for [AppUserDetails]
 */
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
}