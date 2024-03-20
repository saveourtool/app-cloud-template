package com.saveourtool.template.gateway.entities

import com.saveourtool.template.authentication.AppUserDetails
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.security.core.userdetails.UserDetails

/**
 * @property name
 * @property password *in plain text*
 * @property role role of this user
 * @property email email of user
 */
@Entity
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String,
    var password: String?,
    var role: String,
    var email: String? = null,
) {
    /**
     * @return [id] as not null with validating
     * @throws IllegalArgumentException when [id] is not set that means entity is not saved yet
     */
    fun requiredId(): Long = requireNotNull(id) {
        "Entity is not saved yet: $this"
    }

    /**
     * @return
     */
    fun toUserDetails(): UserDetails = AppUserDetails(
        requiredId(),
        name,
        role,
    )
}
