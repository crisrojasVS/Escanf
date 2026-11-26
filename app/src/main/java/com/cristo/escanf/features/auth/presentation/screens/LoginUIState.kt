package com.cristo.escanf.features.auth.presentation.screens

import com.cristo.escanf.core.session.UserRole

data class LoginUIState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val loggedInRole: UserRole? = null
)