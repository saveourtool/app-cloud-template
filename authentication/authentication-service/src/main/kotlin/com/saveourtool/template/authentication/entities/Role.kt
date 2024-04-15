package com.saveourtool.template.authentication.entities

/**
 * User roles
 * @property formattedName string representation of the [Role] that should be printed
 * @property priority
 */
enum class Role(
    val formattedName: String,
    private val priority: Int,
) {
    /**
     * Has no role (synonym to null)
     */
    NONE("None", 0),

    /**
     * Has readonly access to public projects.
     */
    VIEWER("Viewer", 1),

    /**
     * admin in organization
     */
    ADMIN("Admin", 2),

    /**
     * User that has created this project
     */
    OWNER("Owner", 3),
    ;

    /**
     * @return this role with default prefix for spring-security
     */
    fun asSpringSecurityRole() = "ROLE_$name"

    companion object {
        /**
         * @param springSecurityRole
         * @return [Role] found by [springSecurityRole] using [asSpringSecurityRole]
         */
        fun fromSpringSecurityRole(springSecurityRole: String): Role? = entries.find { it.asSpringSecurityRole() == springSecurityRole }
    }
}