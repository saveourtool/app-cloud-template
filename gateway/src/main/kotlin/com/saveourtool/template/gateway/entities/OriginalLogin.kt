package com.saveourtool.template.gateway.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

/**
 * @property name
 * @property user
 * @property source
 */
@Entity
class OriginalLogin(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String,
    var source: String,
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User,
)
