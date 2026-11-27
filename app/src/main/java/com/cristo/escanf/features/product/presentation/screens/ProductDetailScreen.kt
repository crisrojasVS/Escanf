package com.cristo.escanf.features.product.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.cristo.escanf.features.product.domain.entities.Product
import com.cristo.escanf.features.product.presentation.components.NutriScoreBadge
import com.cristo.escanf.features.product.presentation.viewmodels.ProductDetailVM
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    onBack: () -> Unit,
    viewModel: ProductDetailVM = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val product = state.product

    Scaffold(
        // El Scaffold de MainAppScreen ya aplica los márgenes del sistema
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = { Text("Producto") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
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
                product != null -> ProductContent(product, state.saveError)
                else -> ErrorContent(
                    message = state.error ?: "Ocurrió un error",
                    onRetry = viewModel::load,
                    onBack = onBack,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun ProductContent(product: Product, saveError: String?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        product.imageUrl?.let { url ->
            AsyncImage(
                model = url,
                contentDescription = product.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }

        Column {
            Text(product.name, style = MaterialTheme.typography.titleLarge)
            Text(
                text = listOf(product.brand, product.quantity)
                    .filter { it.isNotBlank() }
                    .joinToString(" · "),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Nutri-Score", style = MaterialTheme.typography.labelLarge)
            NutriScoreBadge(score = product.nutriScore)
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Calorías", style = MaterialTheme.typography.labelLarge)
                Text(
                    text = product.caloriesPer100g
                        ?.let { String.format(Locale.getDefault(), "%.0f kcal", it) }
                        ?: "No disponible",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text("por cada 100 g", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Información nutrimental (por 100 g)", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(8.dp))
                NutrientRow("Proteínas", product.proteins)
                NutrientRow("Carbohidratos", product.carbohydrates)
                NutrientRow("Azúcares", product.sugars)
                NutrientRow("Grasas", product.fat)
                NutrientRow("Grasas saturadas", product.saturatedFat)
                NutrientRow("Fibra", product.fiber)
                NutrientRow("Sal", product.salt)
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Ingredientes", style = MaterialTheme.typography.labelLarge)
            Text(
                text = product.ingredients.ifBlank { "Ingredientes no disponibles" },
                style = MaterialTheme.typography.bodyLarge
            )
        }

        saveError?.let {
            Text(
                text = "No se guardó en tu historial: $it",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun NutrientRow(label: String, value: Double?) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = value?.let { String.format(Locale.getDefault(), "%.1f g", it) } ?: "—",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        HorizontalDivider()
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(message, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
        Button(onClick = onRetry) { Text("Reintentar") }
        OutlinedButton(onClick = onBack) { Text("Volver a escanear") }
    }
}