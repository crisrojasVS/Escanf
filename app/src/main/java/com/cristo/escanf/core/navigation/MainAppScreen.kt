package com.cristo.escanf.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cristo.escanf.core.session.SessionState
import com.cristo.escanf.core.session.SessionViewModel
import com.cristo.escanf.core.session.UserRole

@Composable
fun MainAppScreen(sessionViewModel: SessionViewModel = hiltViewModel()) {
    val sessionState by sessionViewModel.state.collectAsStateWithLifecycle()

    when (sessionState) {
        SessionState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        else -> MainContent(sessionState)
    }
}

@Composable
private fun MainContent(sessionState: SessionState) {
    val navController = rememberNavController()

    // Se calcula una sola vez: la sesión que había al abrir la app.
    val startDestination = remember {
        when (sessionState) {
            is SessionState.LoggedIn ->
                if (sessionState.role == UserRole.ADMIN) Screen.Admin.route
                else Screen.Scanner.route
            else -> Screen.Login.route
        }
    }

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val showBottomBar = Screen.userBottomItems.any { it.route == currentRoute }

    // Si se cierra la sesión (logout), regresar al login.
    LaunchedEffect(sessionState) {
        if (sessionState is SessionState.LoggedOut) {
            val route = navController.currentDestination?.route
            if (route != null && route != Screen.Login.route && route != Screen.Register.route) {
                navController.navigate(Screen.Login.route) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                AppBottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            startDestination = startDestination
        )
    }
}