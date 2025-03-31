package com.traderbird.repository

import com.traderbird.model.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProfileRepository : JpaRepository<Profile, Long> {
    fun findByNickname(nickname: String): Profile?
    fun findByEmail(email: String): Profile?
    fun findByDescription(description: String): Profile?
}