package mx.edu.utng.prgs.smarthealthmonitor2.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.edu.utng.prgs.smarthealthmonitor2.data.models.LecturaFC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilaHistorial(
    lectura: LecturaFC,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${lectura.valorBpm} bpm",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (lectura.esNormal)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.error
            )
            Text(
                text = lectura.hora,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    }
}

@Preview(showBackground = true, name = "FilaHistorial - Normal")
@Preview(showBackground = true, name = "FilaHistorial - Fuera de rango")
@Composable
private fun FilaHistorialPreview() {
    MaterialTheme {
        Column {
            FilaHistorial(
                lectura = LecturaFC(1, 78, "11:00", true)
            )
            FilaHistorial(
                lectura = LecturaFC(4, 95, "09:30", false)
            )
        }
    }
}