package com.cristo.escanf.features.product.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cristo.escanf.features.product.domain.entities.NutriScore
import com.cristo.escanf.ui.theme.NutriA
import com.cristo.escanf.ui.theme.NutriB
import com.cristo.escanf.ui.theme.NutriC
import com.cristo.escanf.ui.theme.NutriD
import com.cristo.escanf.ui.theme.NutriE
import com.cristo.escanf.ui.theme.NutriUnknown

fun nutriScoreColor(score: NutriScore): Color = when (score) {
    NutriScore.A -> NutriA
    NutriScore.B -> NutriB
    NutriScore.C -> NutriC
    NutriScore.D -> NutriD
    NutriScore.E -> NutriE
    NutriScore.UNKNOWN -> NutriUnknown
}

@Composable
fun NutriScoreBadge(
    score: NutriScore,
    modifier: Modifier = Modifier
) {
    if (score == NutriScore.UNKNOWN) {
        Text(
            text = "Nutri-Score no disponible",
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier
        )
        return
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(NutriScore.A, NutriScore.B, NutriScore.C, NutriScore.D, NutriScore.E).forEach { item ->
            val selected = item == score
            Box(
                modifier = Modifier
                    .size(if (selected) 46.dp else 32.dp)
                    .clip(CircleShape)
                    .background(nutriScoreColor(item).copy(alpha = if (selected) 1f else 0.35f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = if (selected) 22.sp else 14.sp
                )
            }
        }
    }
}