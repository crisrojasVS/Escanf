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
        // TODO: cada Placeholder se reemplaza por su pantalla real en commits posteriores
        composable(Screen.Login.route) { Placeholder("Login") }
        composable(Screen.Register.route) { Placeholder("Registro") }
        composable(Screen.Scanner.route) { Placeholder("Escáner") }
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