package mx.edu.utng.tv.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.Surface
import mx.edu.utng.tv.LecturaFC
import androidx.tv.material3.ExperimentalTvMaterial3Api  // ← AGREGAR

@OptIn(ExperimentalTvMaterial3Api::class)  // ← AGREGAR
@Composable
fun FcCardItem(
    lectura: LecturaFC,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.width(220.dp).height(140.dp),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = if (lectura.esNormal) Color(0xFF1565C0) else Color(0xFFB71C1C),
            focusedContainerColor = if (lectura.esNormal) Color(0xFF42A5F5) else Color(0xFFE53935),
            pressedContainerColor = if (lectura.esNormal) Color(0xFF0D47A1) else Color(0xFF880E4F)
        ),
        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${lectura.valorBpm} bpm",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Column {
                Text(
                    text = if (lectura.esNormal) "✅ Normal" else "⚠️ Alerta",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = lectura.hora,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }
    }
}