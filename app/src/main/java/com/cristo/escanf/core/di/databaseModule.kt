package com.cristo.escanf.core.di

import android.content.Context
import androidx.room.Room
import com.cristo.escanf.core.database.AppDatabase
import com.cristo.escanf.features.auth.data.datasources.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "escanf.db")
            .fallbackToDestructiveMigration(true) // solo para desarrollo
            .build()
    }

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
}