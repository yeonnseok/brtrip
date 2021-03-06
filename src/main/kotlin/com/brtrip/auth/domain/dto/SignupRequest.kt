package com.brtrip.auth.domain.dto

import com.brtrip.user.domain.AuthProvider
import com.brtrip.user.domain.RoleType
import com.brtrip.user.domain.User
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class SignupRequest(
    @NotBlank
    @Email
    val email: String,

    @NotBlank
    val password: String,

    @NotBlank
    val passwordCheck: String,

    val role: String = RoleType.ROLE_USER.name,

    val authProvider: String = AuthProvider.LOCAL.name
) {
    fun toEntity(encodedPw: String): User {
        return User(
            email = email,
            password = encodedPw,
            role = RoleType.valueOf(role),
            authProvider = AuthProvider.of(authProvider)
        )
    }
}
