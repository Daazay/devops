package org.daazay.presentation.mapper.auth

import org.daazay.domain.model.auth.User
import org.daazay.presentation.response.auth.UserResponse

fun User.toUserResponse() = UserResponse(
    id = id.toString(),
    username = username,
    email = email,
    role = role.name,
    createdAt = createdAt.toString(),
    updatedAt = updatedAt.toString(),
)