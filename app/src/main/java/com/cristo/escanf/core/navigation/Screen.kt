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

    data object ProductDetail : Screen("product/{barcode}?save={save}", "Producto") {
        const val ARG_BARCODE = "barcode"
        const val ARG_SAVE = "save"
        fun createRoute(barcode: String, save: Boolean = true) = "product/$barcode?save=$save"
    }

    companion object {
        // Pestañas de la barra inferior (rol USER).
        // Es un getter a propósito: si fuera un "val = listOf(...)" se evaluaría
        // durante la inicialización de Screen y podría contener nulls.
        val userBottomItems: List<Screen>
            get() = listOf(Scanner, History)
    }
}