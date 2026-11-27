package com.cristo.escanf.features.admin.di

import com.cristo.escanf.core.di.EscanfRetrofit
import com.cristo.escanf.features.admin.data.datasources.remote.api.AdminApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdminNetworkModule {
    @Provides
    @Singleton
    fun provideAdminApi(@EscanfRetrofit retrofit: Retrofit): AdminApi {
        return retrofit.create(AdminApi::class.java)
    }
}