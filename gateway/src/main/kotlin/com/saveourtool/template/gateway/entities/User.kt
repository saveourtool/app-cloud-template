package com.saveourtool.template.gateway.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

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
    var role: String?,
    var email: String? = null,
)
