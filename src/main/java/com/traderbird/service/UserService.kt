package com.traderbird.service

import com.traderbird.repository.UserRepository
import com.traderbird.model.User
import com.traderbird.model.UserUtils.RoleNames
import com.traderbird.model.getRoleList
import com.traderbird.model.onDelete
import com.traderbird.model.onSave
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserDetailsService {

    fun saveUser(user: User): User {
        val encodedPassword = passwordEncoder.encode(user.password)
        val userToSave = user.copy(password = encodedPassword)
        user.onSave()
        return userRepository.save(userToSave)
    }

    fun deleteUser(user: User) {
        user.onDelete()
        userRepository.delete(user)
    }

    @Transactional
    open fun updatePassword(user: User, newPassword: String): Boolean {
        val encodedPassword = passwordEncoder.encode(newPassword)
        val updatedUser = user.copy(password = encodedPassword)
        userRepository.save(updatedUser)
        return true
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found")
        return org.springframework.security.core.userdetails.User(
            user.username,
            user.password,
            user.getRoleList().map { SimpleGrantedAuthority("${RoleNames[it]}") }
        )
    }

    fun existsByUsername(username: String): Boolean {
        return userRepository.findByUsername(username) != null
    }

    fun getUserByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun checkPassword(user: User, rawPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, user.password)
    }

    fun getUserById(id: Long): User? {
        return userRepository.findById(id).orElse(null)
    }

    fun save(user: User): User {
        return userRepository.save(user)
    }
}