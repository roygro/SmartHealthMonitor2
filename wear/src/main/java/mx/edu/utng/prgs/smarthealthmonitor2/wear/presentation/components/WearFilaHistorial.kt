package mx.edu.utng.prgs.smarthealthmonitor2.wear.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth  // ← AGREGAR ESTO
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import mx.edu.utng.prgs.smarthealthmonitor2.wear.presentation.LecturaFC

@Composable
fun WearFilaHistorial(lectura: LecturaFC) {
    val color = if (lectura.esNormal)
        MaterialTheme.colors.primary
    else
        MaterialTheme.colors.error

    Chip(
        label = {
            Text(
                text = "${lectura.valorBpm} bpm",
                color = color
            )
        },
        secondaryLabel = {
            Text(text = lectura.hora)
        },
        onClick = { },
        colors = ChipDefaults.secondaryChipColors(),
        modifier = Modifier.fillMaxWidth()
    )
}