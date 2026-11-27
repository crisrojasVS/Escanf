package com.cristo.escanf.features.history.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cristo.escanf.features.history.presentation.components.HistoryCard
import com.cristo.escanf.features.history.presentation.viewmodels.HistoryVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onOpenProduct: (String) -> Unit,
    onLogout: () -> Unit,
    viewModel: HistoryVM = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showClearDialog by remember { mutableStateOf(false) }

    // Al volver a esta pestaña se recarga, así aparecen los escaneos nuevos
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.refresh() }

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
            TopAppBar(
                title = { Text("Mi historial") },
                actions = {
                    IconButton(
                        onClick = { showClearDialog = true },
                        enabled = state.items.isNotEmpty()
                    ) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = "Vaciar historial")
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
                    }
                },
                windowInsets = WindowInsets(0, 0, 0, 0)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))

                state.items.isEmpty() -> EmptyOrError(
                    error = state.error,
                    onRetry = viewModel::refresh,
                    modifier = Modifier.align(Alignment.Center)
                )

                else -> HistoryList(
                    state = state,
                    onLoadMore = viewModel::loadMore,
                    onOpenProduct = onOpenProduct,
                    onDelete = viewModel::delete
                )
            }
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Vaciar historial") },
            text = { Text("Se borrarán todos tus escaneos. Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    showClearDialog = false
                    viewModel.clearAll()
                }) { Text("Vaciar") }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun HistoryList(
    state: HistoryUIState,
    onLoadMore: () -> Unit,
    onOpenProduct: (String) -> Unit,
    onDelete: (Long) -> Unit
) {
    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            state.hasMore && lastVisible >= state.items.size - 3
        }
    }
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) onLoadMore()
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(state.items, key = { it.id }) { record ->
            HistoryCard(
                record = record,
                onClick = { onOpenProduct(record.barcode) },
                onDelete = { onDelete(record.id) }
            )
        }
        if (state.isLoadingMore) {
            item {
                Box(Modifier.fillMaxWidth().padding(8.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun EmptyOrError(
    error: String?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = error ?: "Aún no has escaneado productos",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        if (error != null) {
            Button(onClick = onRetry) { Text("Reintentar") }
        }
    }
}