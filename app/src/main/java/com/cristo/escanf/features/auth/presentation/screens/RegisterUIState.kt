package com.cristo.escanf.features.auth.presentation.screens

data class RegisterUIState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val registered: Boolean = false
)