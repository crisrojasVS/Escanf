package com.cristo.escanf.features.data.datasource.remote.mapper

import com.cristo.escanf.core.session.UserRole
import com.cristo.escanf.features.auth.data.datasources.remote.models.UserDto
import com.cristo.escanf.features.auth.domain.entities.User

fun UserDto.toDomain(): User {
    return User(
        id = this.id,
        name = this.name,
        email = this.email,
        role = UserRole.valueOf(this.role),
        isBlocked = this.isBlocked
    )
}