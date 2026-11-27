package com.cristo.escanf.features.history.di

import com.cristo.escanf.features.history.data.repositories.HistoryRepositoryImpl
import com.cristo.escanf.features.history.domain.repositories.HistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HistoryRepositoryModule {
    @Binds
    abstract fun bindHistoryRepository(
        historyRepositoryImpl: HistoryRepositoryImpl
    ): HistoryRepository
}