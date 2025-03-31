package com.traderbird.model

import com.traderbird.ksp.CollectFunction
import com.traderbird.service.UserService
import com.traderbird.util.GetSpringBean
import jakarta.persistence.*
import org.springframework.security.core.context.SecurityContextHolder

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "username", nullable = false, unique = true)
    val username: String = "",

    @Column(name = "password", nullable = false)
    var password: String = "",

    @Column(name = "roles", nullable = false)
    val roles: Int = 0,

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "profile")
    var profile: Profile = Profile(),

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "author")
    @OrderBy("createdAt DESC")
    var posts: MutableList<Post> = mutableListOf(),

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    var favorites: MutableList<Favorite> = mutableListOf(),
)

enum class Role {
    ADMIN, USER, DEVELOPER
}

object UserUtils {
    val RoleNames = mapOf(
        Role.ADMIN to "ROLE_ADMIN",
        Role.USER to "ROLE_USER",
        Role.DEVELOPER to "ROLE_DEVELOPER"
    )

    @JvmStatic
    val onUserDeleteAction = mutableListOf<(User) -> Unit>()

    @JvmStatic
    fun onUserDelete(user: User) {
        onUserDeleteAction.forEach { it -> it(user) }
    }

    @JvmStatic
    @CollectFunction(targetClass = UserUtils::class, targetField = "onUserDeleteAction")
    fun testUserDelete(user: User) {
        println("User ${user.username} deleted from kotlin")
    }

    @JvmStatic
    val onUserSaveAction = mutableListOf<(User) -> Unit>()

    @JvmStatic
    fun onUserSave(user: User) {
        onUserSaveAction.forEach { it -> it(user) }
    }

    @JvmStatic
    fun getNowAuthenticatedUser(): User? {
        val authentication = SecurityContextHolder.getContext().authentication

        authentication?.let {
            val username = it.name
            val userService = GetSpringBean(UserService::class.java)
            return userService.getUserByUsername(username)
        } ?: return null
    }
}

fun User.getRoleList(): List<Role> {
    val roles = mutableListOf<Role>()
    if (this.roles and 1 == 1) roles.add(Role.ADMIN)
    if (this.roles and 2 == 2) roles.add(Role.USER)
    if (this.roles and 4 == 4) roles.add(Role.DEVELOPER)
    return roles
}

fun User.asResponseObject(): Map<String, Any> {
    return mapOf(
        "id" to id,
        "username" to username,
        "roles" to getRoleList().map { it.name },
        "profile" to profile
    )
}

fun User.onDelete() {
    UserUtils.onUserDelete(this)
}

fun User.onSave() {
    UserUtils.onUserSave(this)
}

data class UserBuilder(
    private var id: Long = 0,
    private var username: String = "",
    private var password: String = "",
    private var roles: MutableList<Role> = mutableListOf(),
    private var profile: Profile? = null
) {
    fun id(id: Long) = apply { this.id = id }
    fun username(username: String) = apply { this.username = username }
    fun password(password: String) = apply { this.password = password }
    fun addRole(role: Role) = apply { roles.add(role) }
    fun addProfile(profile: Profile) = apply { this.profile = profile }

    fun build(): User {
        if (username.isEmpty() || profile == null)
            throw IllegalArgumentException("Username and profile are required")
        return User(id, username, password, getRoleCombination(roles), profile!!)
    }

    companion object {
        fun getRoleCombination(roles: List<Role>): Int {
            var combination = 0
            for (role in roles) {
                combination = combination or when (role) {
                    Role.ADMIN -> 1
                    Role.USER -> 2
                    Role.DEVELOPER -> 4
                }
            }
            return combination
        }
    }
}