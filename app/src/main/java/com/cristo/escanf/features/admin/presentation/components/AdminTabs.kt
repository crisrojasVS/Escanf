package com.cristo.escanf.features.admin.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cristo.escanf.features.admin.domain.entities.AdminScan
import com.cristo.escanf.features.admin.domain.entities.ManagedUser
import com.cristo.escanf.features.admin.presentation.screens.AdminUIState
import com.cristo.escanf.features.history.presentation.components.MiniNutriScore
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

private val dateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
        .withZone(ZoneId.systemDefault())

// ------------------------------------------------------------------ Resumen

@Composable
fun StatsTab(state: AdminUIState, onRetry: () -> Unit) {
    val stats = state.stats
    when {
        stats != null -> Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Usuarios", stats.totalUsers, Modifier.weight(1f))
                StatCard("Bloqueados", stats.blockedUsers, Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Escaneos totales", stats.totalScans, Modifier.weight(1f))
                StatCard("Escaneos hoy", stats.scansToday, Modifier.weight(1f))
            }

            Text(
                text = "Productos más escaneados",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
            if (stats.topProducts.isEmpty()) {
                Text("Aún no hay escaneos", style = MaterialTheme.typography.bodyLarge)
            } else {
                stats.topProducts.forEachIndexed { index, product ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                "${index + 1}",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    product.productName,
                                    style = MaterialTheme.typography.labelLarge,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(product.barcode, style = MaterialTheme.typography.bodyLarge)
                            }
                            Text(
                                "${product.scans}",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                }
            }
        }

        state.statsLoading -> LoadingBox()

        else -> CenteredMessage(
            message = state.statsError ?: "No se pudieron cargar las estadísticas",
            onRetry = onRetry
        )
    }
}

@Composable
private fun StatCard(label: String, value: Int, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(Modifier.padding(16.dp)) {
            Text(label, style = MaterialTheme.typography.labelLarge)
            Text(
                text = "$value",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// ------------------------------------------------------------------ Usuarios

@Composable
fun UsersTab(
    state: AdminUIState,
    onSearchChange: (String) -> Unit,
    onSearch: () -> Unit,
    onToggleBlock: (ManagedUser) -> Unit,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = state.search,
            onValueChange = onSearchChange,
            label = { Text("Buscar por nombre o correo") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
            trailingIcon = {
                IconButton(onClick = onSearch) {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when {
                state.usersLoading -> LoadingBox()

                state.users.isEmpty() -> CenteredMessage(
                    message = state.usersError ?: "No hay usuarios que mostrar",
                    onRetry = if (state.usersError != null) onRetry else null
                )

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.users, key = { it.id }) { user ->
                        UserCard(
                            user = user,
                            busy = user.id in state.busyUserIds,
                            onToggleBlock = { onToggleBlock(user) }
                        )
                    }
                    if (state.usersHasMore) {
                        item {
                            OutlinedButton(
                                onClick = onLoadMore,
                                enabled = !state.usersLoadingMore,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(if (state.usersLoadingMore) "Cargando…" else "Cargar más")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserCard(user: ManagedUser, busy: Boolean, onToggleBlock: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(user.name, style = MaterialTheme.typography.labelLarge)
                    Text(user.email, style = MaterialTheme.typography.bodyLarge)
                }
                if (user.isBlocked) {
                    Text(
                        "BLOQUEADO",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            Text("${user.scanCount} escaneos", style = MaterialTheme.typography.bodyLarge)

            if (user.isBlocked) {
                Button(onClick = onToggleBlock, enabled = !busy) { Text("Desbloquear") }
            } else {
                OutlinedButton(
                    onClick = onToggleBlock,
                    enabled = !busy,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) { Text("Bloquear") }
            }
        }
    }
}

// ------------------------------------------------------------------ Escaneos

@Composable
fun ScansTab(
    state: AdminUIState,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.scansLoading -> LoadingBox()

            state.scans.isEmpty() -> CenteredMessage(
                message = state.scansError ?: "Aún no hay escaneos",
                onRetry = if (state.scansError != null) onRetry else null
            )

            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.scans, key = { it.record.id }) { scan ->
                    AdminScanCard(scan)
                }
                if (state.scansHasMore) {
                    item {
                        OutlinedButton(
                            onClick = onLoadMore,
                            enabled = !state.scansLoadingMore,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (state.scansLoadingMore) "Cargando…" else "Cargar más")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminScanCard(scan: AdminScan) {
    val record = scan.record
    val date = remember(record.scannedAtMillis) {
        dateFormatter.format(Instant.ofEpochMilli(record.scannedAtMillis))
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (record.imageUrl != null) {
                AsyncImage(
                    model = record.imageUrl,
                    contentDescription = record.productName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    record.productName,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(record.brand, style = MaterialTheme.typography.bodyLarge, maxLines = 1)
                Text(
                    text = "Por: ${scan.userName} (${scan.userEmail})",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = buildString {
                        append(date)
                        record.calories?.let {
                            append(" · ")
                            append(String.format(Locale.getDefault(), "%.0f kcal", it))
                        }
                    },
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            MiniNutriScore(record.nutriScore)
        }
    }
}

// ------------------------------------------------------------------ Comunes

@Composable
private fun LoadingBox() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun CenteredMessage(message: String, onRetry: (() -> Unit)?) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(message, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
            if (onRetry != null) {
                Button(onClick = onRetry) { Text("Reintentar") }
            }
        }
    }
}