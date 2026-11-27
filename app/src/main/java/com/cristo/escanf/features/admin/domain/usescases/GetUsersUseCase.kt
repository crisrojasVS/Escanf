package com.cristo.escanf.features.admin.domain.usescases

import com.cristo.escanf.features.admin.domain.entities.UsersPage
import com.cristo.escanf.features.admin.domain.repositories.AdminRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(search: String, page: Int): Result<UsersPage> {
        return try {
            Result.success(repository.getUsers(search, page))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}