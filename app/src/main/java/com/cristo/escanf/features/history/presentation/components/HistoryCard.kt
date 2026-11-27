package com.cristo.escanf.features.history.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cristo.escanf.features.history.domain.entities.ScanRecord
import com.cristo.escanf.features.product.domain.entities.NutriScore
import com.cristo.escanf.features.product.presentation.components.nutriScoreColor
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

private val dateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
        .withZone(ZoneId.systemDefault())

@Composable
fun HistoryCard(
    record: ScanRecord,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val date = remember(record.scannedAtMillis) {
        dateFormatter.format(Instant.ofEpochMilli(record.scannedAtMillis))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
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
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = record.productName,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = record.brand,
                    style = MaterialTheme.typography.bodyLarge,
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
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 12.sp)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                MiniNutriScore(record.nutriScore)
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Borrar del historial")
                }
            }
        }
    }
}

@Composable
fun MiniNutriScore(score: NutriScore) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(nutriScoreColor(score)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (score == NutriScore.UNKNOWN) "–" else score.name,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}