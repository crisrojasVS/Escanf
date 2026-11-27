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
        val TOKEN = stringPreferencesKey("token")
        val USER_ID = longPreferencesKey("user_id")
        val NAME = stringPreferencesKey("name")
        val EMAIL = stringPreferencesKey("email")
        val ROLE = stringPreferencesKey("role")
    }

    /** Emite null cuando no hay sesión iniciada. */
    val session: Flow<Session?> = context.sessionDataStore.data.map { prefs ->
        val token = prefs[Keys.TOKEN]
        val id = prefs[Keys.USER_ID]
        val role = prefs[Keys.ROLE]
        if (token != null && id != null && role != null) {
            Session(
                token = token,
                userId = id,
                name = prefs[Keys.NAME].orEmpty(),
                email = prefs[Keys.EMAIL].orEmpty(),
                role = UserRole.valueOf(role)
            )
        } else {
            null
        }
    }

    suspend fun save(
        token: String,
        userId: Long,
        name: String,
        email: String,
        role: UserRole
    ) {
        context.sessionDataStore.edit { prefs ->
            prefs[Keys.TOKEN] = token
            prefs[Keys.USER_ID] = userId
            prefs[Keys.NAME] = name
            prefs[Keys.EMAIL] = email
            prefs[Keys.ROLE] = role.name
        }
    }

    suspend fun clear() {
        context.sessionDataStore.edit { it.clear() }
    }
}