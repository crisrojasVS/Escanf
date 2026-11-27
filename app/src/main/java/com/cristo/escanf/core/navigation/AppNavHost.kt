package com.cristo.escanf.core.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cristo.escanf.core.session.UserRole
import com.cristo.escanf.features.auth.presentation.screens.LoginScreen
import com.cristo.escanf.features.auth.presentation.screens.RegisterScreen
import com.cristo.escanf.features.history.presentation.screens.HistoryScreen
import com.cristo.escanf.features.product.presentation.screens.ProductDetailScreen
import com.cristo.escanf.features.scanner.presentation.screens.ScannerScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { role ->
                    val destination =
                        if (role == UserRole.ADMIN) Screen.Admin.route else Screen.Scanner.route
                    navController.navigate(destination) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegistered = {
                    navController.navigate(Screen.Scanner.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Scanner.route) {
            ScannerScreen(
                onBarcodeScanned = { barcode ->
                    navController.navigate(Screen.ProductDetail.createRoute(barcode))
                },
                onLogout = onLogout
            )
        }
        composable(Screen.History.route) {
            HistoryScreen(
                onOpenProduct = { barcode ->
                    // save = false: abrir desde el historial no crea otro registro
                    navController.navigate(Screen.ProductDetail.createRoute(barcode, save = false))
                },
                onLogout = onLogout
            )
        }
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(
                navArgument(Screen.ProductDetail.ARG_BARCODE) { type = NavType.StringType },
                navArgument(Screen.ProductDetail.ARG_SAVE) {
                    type = NavType.BoolType
                    defaultValue = true
                }
            )
        ) {
            ProductDetailScreen(onBack = { navController.popBackStack() })
        }

        // TODO: se reemplaza por el panel real en el siguiente commit
        composable(Screen.Admin.route) { AdminPlaceholder(onLogout) }
    }
}

@Composable
private fun AdminPlaceholder(onLogout: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Panel de administrador")
        Button(onClick = onLogout) { Text("Cerrar sesión") }
    }
}