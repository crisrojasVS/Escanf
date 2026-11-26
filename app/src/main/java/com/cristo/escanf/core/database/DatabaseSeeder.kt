package com.cristo.escanf.core.database

import com.cristo.escanf.core.security.PasswordHasher
import com.cristo.escanf.core.session.UserRole
import com.cristo.escanf.features.auth.data.datasources.local.dao.UserDao
import com.cristo.escanf.features.auth.data.datasources.local.entities.UserEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseSeeder @Inject constructor(
    private val userDao: UserDao,
    private val hasher: PasswordHasher
) {
    suspend fun seedAdmin() {
        if (userDao.findByEmail(ADMIN_EMAIL) != null) return
        userDao.insert(
            UserEntity(
                name = "Administrador",
                email = ADMIN_EMAIL,
                passwordHash = hasher.hash(ADMIN_PASSWORD),
                role = UserRole.ADMIN.name
            )
        )
    }

    companion object {
        const val ADMIN_EMAIL = "admin@escanf.com"
        const val ADMIN_PASSWORD = "admin123"
    }
}