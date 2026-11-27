package com.cristo.escanf.features.admin.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cristo.escanf.features.admin.presentation.components.ScansTab
import com.cristo.escanf.features.admin.presentation.components.StatsTab
import com.cristo.escanf.features.admin.presentation.components.UsersTab
import com.cristo.escanf.features.admin.presentation.viewmodels.AdminViewModel

private val tabTitles = listOf("Resumen", "Usuarios", "Escaneos")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    onLogout: () -> Unit,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        state.message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.consumeMessage()
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Panel de administrador") },
                    actions = {
                        IconButton(onClick = viewModel::refreshCurrent) {
                            Icon(Icons.Default.Refresh, contentDescription = "Actualizar")
                        }
                        IconButton(onClick = onLogout) {
                            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
                        }
                    },
                    windowInsets = WindowInsets(0, 0, 0, 0)
                )
                TabRow(selectedTabIndex = state.selectedTab) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = state.selectedTab == index,
                            onClick = { viewModel.selectTab(index) },
                            text = { Text(title) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (state.selectedTab) {
                0 -> StatsTab(state = state, onRetry = viewModel::loadStats)
                1 -> UsersTab(
                    state = state,
                    onSearchChange = viewModel::onSearchChange,
                    onSearch = viewModel::searchUsers,
                    onToggleBlock = viewModel::toggleBlock,
                    onLoadMore = viewModel::loadMoreUsers,
                    onRetry = { viewModel.loadUsers(showSpinner = true) }
                )
                else -> ScansTab(
                    state = state,
                    onLoadMore = viewModel::loadMoreScans,
                    onRetry = viewModel::loadScans
                )
            }
        }
    }
}