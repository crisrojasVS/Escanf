package com.cristo.escanf.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cristo.escanf.features.auth.data.datasources.local.dao.UserDao
import com.cristo.escanf.features.auth.data.datasources.local.entities.UserEntity

// Aquí se irán agregando las entidades de historial en commits posteriores.
@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}