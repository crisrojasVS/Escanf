package com.cristo.escanf.features.auth.data.repositories

import com.cristo.escanf.core.security.PasswordHasher
import com.cristo.escanf.core.session.SessionManager
import com.cristo.escanf.core.session.UserRole
import com.cristo.escanf.features.auth.data.datasources.local.dao.UserDao
import com.cristo.escanf.features.auth.data.datasources.local.entities.UserEntity
import com.cristo.escanf.features.auth.data.datasources.local.mapper.toDomain
import com.cristo.escanf.features.auth.domain.entities.AuthException
import com.cristo.escanf.features.auth.domain.entities.User
import com.cristo.escanf.features.auth.domain.repositories.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val hasher: PasswordHasher,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): User {
        val entity = userDao.findByEmail(email)
            ?: throw AuthException("Correo o contraseña incorrectos")

        val valid = withContext(Dispatchers.Default) {
            hasher.verify(password, entity.passwordHash)
        }
        if (!valid) throw AuthException("Correo o contraseña incorrectos")

        if (entity.isBlocked) {
            throw AuthException("Tu cuenta está bloqueada. Contacta al administrador")
        }

        val user = entity.toDomain()
        sessionManager.save(user.id, user.role)
        return user
    }

    override suspend fun register(name: String, email: String, password: String): User {
        if (userDao.findByEmail(email) != null) {
            throw AuthException("Ya existe una cuenta con ese correo")
        }

        val hash = withContext(Dispatchers.Default) { hasher.hash(password) }
        val id = userDao.insert(
            UserEntity(
                name = name,
                email = email,
                passwordHash = hash,
                role = UserRole.USER.name
            )
        )

        val user = User(id = id, name = name, email = email, role = UserRole.USER, isBlocked = false)
        sessionManager.save(user.id, user.role)
        return user
    }

    override suspend fun logout() {
        sessionManager.clear()
    }
}