package com.cristo.escanf.core.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.sessionDataStore by preferencesDataStore(name = "session")

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val USER_ID = longPreferencesKey("user_id")
        val ROLE = stringPreferencesKey("role")
    }

    /** Emite null cuando no hay sesión iniciada. */
    val session: Flow<Session?> = context.sessionDataStore.data.map { prefs ->
        val id = prefs[Keys.USER_ID]
        val role = prefs[Keys.ROLE]
        if (id != null && role != null) {
            Session(userId = id, role = UserRole.valueOf(role))
        } else {
            null
        }
    }

    suspend fun save(userId: Long, role: UserRole) {
        context.sessionDataStore.edit { prefs ->
            prefs[Keys.USER_ID] = userId
            prefs[Keys.ROLE] = role.name
        }
    }

    suspend fun clear() {
        context.sessionDataStore.edit { it.clear() }
    }
}