package com.cristo.escanf.features.admin.di

import com.cristo.escanf.features.admin.data.repositories.AdminRepositoryImpl
import com.cristo.escanf.features.admin.domain.repositories.AdminRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AdminRepositoryModule {
    @Binds
    abstract fun bindAdminRepository(
        adminRepositoryImpl: AdminRepositoryImpl
    ): AdminRepository
}