package com.cristo.escanf.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector? = null
) {
    data object Login : Screen("login", "Iniciar sesión")
    data object Register : Screen("register", "Crear cuenta")

    data object Scanner : Screen("scanner", "Escanear", Icons.Default.QrCodeScanner)
    data object History : Screen("history", "Historial", Icons.Default.History)
    data object Admin : Screen("admin", "Administrador", Icons.Default.AdminPanelSettings)

    data object ProductDetail : Screen("product/{barcode}", "Producto") {
        const val ARG_BARCODE = "barcode"
        fun createRoute(barcode: String) = "product/$barcode"
    }

    companion object {
        // Pestañas que se ven en la barra inferior (rol USER)
        val userBottomItems = listOf(Scanner, History)
    }
}