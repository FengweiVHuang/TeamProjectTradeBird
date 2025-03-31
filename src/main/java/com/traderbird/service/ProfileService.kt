package com.traderbird.service

import com.traderbird.model.Profile
import com.traderbird.repository.ProfileRepository

class ProfileService (
    private val profileRepository: ProfileRepository
) {
    fun getProfileByNickname(nickname: String): Profile? {
        return profileRepository.findByNickname(nickname)
    }

    fun getProfileByEmail(email: String): Profile? {
        return profileRepository.findByEmail(email)
    }

    fun getProfileByDescription(description: String): Profile? {
        return profileRepository.findByDescription(description)
    }
}