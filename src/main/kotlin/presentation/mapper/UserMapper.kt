package org.daazay.presentation.mapper

import org.daazay.domain.model.User
import org.daazay.presentation.response.UserResponse

fun User.toUserResponse() = UserResponse(
    id = id.toString(),
    username = username,
    email = email,
    role = role.name,
    createdAt = createdAt.toString(),
    updatedAt = updatedAt.toString(),
)