package com.cristo.escanf.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import com.cristo.escanf.features.scanner.presentation.screens.ScannerScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
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
                }
            )
        }

        // TODO: se reemplazan por las pantallas reales en commits posteriores
        composable(Screen.History.route) { Placeholder("Historial") }
        composable(Screen.Admin.route) { Placeholder("Panel de administrador") }
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(
                navArgument(Screen.ProductDetail.ARG_BARCODE) { type = NavType.StringType }
            )
        ) { Placeholder("Detalle del producto") }
    }
}

@Composable
private fun Placeholder(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text)
    }
}