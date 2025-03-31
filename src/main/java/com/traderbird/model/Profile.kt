package com.traderbird.model

import com.traderbird.ksp.CollectFunction
import com.traderbird.repository.ProfileRepository
import com.traderbird.util.GetSpringBean
import jakarta.persistence.*

@Entity(name = "profiles")
class Profile(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(name = "nickname", nullable = false)
    var nickname: String = "",

    @Column(name = "email", nullable = false)
    var email: String = "",

    @Lob
    @Column(name = "description", nullable = false)
    var description: String = "",
) {
}

class ProfileBuilder {
    private var nickname: String? = null
    private var email: String? = null
    private var description: String? = null

    fun nickname(nickname: String?) = apply { this.nickname = nickname }
    fun email(email: String?) = apply { this.email = email }
    fun description(description: String?) = apply { this.description = description }

    fun build(): Profile {
        if (email == null) throw IllegalArgumentException("Email is required")
        return Profile(0, nickname ?: "", email ?: "", description ?: "")
    }
}

object ProfileUtil {
    @JvmStatic
    @CollectFunction(targetClass = UserUtils::class, targetField = "onUserSaveAction")
    fun onUserSaveToDataSource(user: User) {
        val profile = user.profile
        val profileRepository = GetSpringBean(ProfileRepository::class.java)
        profileRepository.save(profile)
    }
}