package com.cristo.escanf.features.auth.data.datasources.local.mapper

import com.cristo.escanf.core.session.UserRole
import com.cristo.escanf.features.auth.data.datasources.local.entities.UserEntity
import com.cristo.escanf.features.auth.domain.entities.User

fun UserEntity.toDomain(): User {
    return User(
        id = this.id,
        name = this.name,
        email = this.email,
        role = UserRole.valueOf(this.role),
        isBlocked = this.isBlocked
    )
}