package com.cristo.escanf.features.history.di

import com.cristo.escanf.core.di.EscanfRetrofit
import com.cristo.escanf.features.history.data.datasources.remote.api.HistoryApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HistoryNetworkModule {
    @Provides
    @Singleton
    fun provideHistoryApi(@EscanfRetrofit retrofit: Retrofit): HistoryApi {
        return retrofit.create(HistoryApi::class.java)
    }
}