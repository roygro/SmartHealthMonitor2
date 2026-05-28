package mx.edu.utng.prgs.smarthealthmonitor2.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TarjetaDato(
    valor: String,
    unidad: String,
    label: String,
    colorValor: Color,
    modifier: Modifier = Modifier,
    valorNumerico: Int? = null
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = valor,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorValor
                )
                Text(
                    text = unidad,
                    style = MaterialTheme.typography.titleSmall,
                    color = colorValor,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            // ⭐ RETO: Versión MANUAL - sin usar operador "in"
            if (label == "Frecuencia cardíaca" && valorNumerico != null) {
                Spacer(Modifier.height(8.dp))

                // Lógica MANUAL más explícita
                val esNormal = if (valorNumerico >= 60 && valorNumerico <= 100) true else false

                val textoEstado = if (esNormal) "Normal" else "Consulta al médico"
                val colorChip = if (esNormal) Color(0xFF4CAF50) else Color(0xFFF44336)
                val colorContainer = if (esNormal) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)

                AssistChip(
                    onClick = { },
                    label = {
                        Text(
                            text = textoEstado,
                            color = colorChip,
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    modifier = Modifier,
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = colorContainer
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "72 bpm - Normal")
@Composable
private fun Preview72() {
    MaterialTheme {
        TarjetaDato(
            valor = "72",
            unidad = "bpm",
            label = "Frecuencia cardíaca",
            colorValor = MaterialTheme.colorScheme.error,
            valorNumerico = 72
        )
    }
}

@Preview(showBackground = true, name = "95 bpm - Debería ser Alerta")
@Composable
private fun Preview95() {
    MaterialTheme {
        TarjetaDato(
            valor = "95",
            unidad = "bpm",
            label = "Frecuencia cardíaca",
            colorValor = MaterialTheme.colorScheme.error,
            valorNumerico = 95
        )
    }
}

@Preview(showBackground = true, name = "45 bpm - Alerta")
@Composable
private fun Preview45() {
    MaterialTheme {
        TarjetaDato(
            valor = "45",
            unidad = "bpm",
            label = "Frecuencia cardíaca",
            colorValor = MaterialTheme.colorScheme.error,
            valorNumerico = 45
        )
    }
}

@Preview(showBackground = true, name = "Pasos")
@Composable
private fun PreviewPasos() {
    MaterialTheme {
        TarjetaDato(
            valor = "4,250",
            unidad = "pasos",
            label = "Pasos del día",
            colorValor = MaterialTheme.colorScheme.primary,
            valorNumerico = null
        )
    }
}