package mx.edu.utng.tv.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import mx.edu.utng.tv.TvViewModel

@Composable
fun TvCatalogScreen(
    onCardClick: (Int) -> Unit = {},
    viewModel: TvViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B4A))
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
            return@Box
        }

        TvLazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 48.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // ── Fila 1: FC actual ──
            item {
                RowSection(
                    title = "⚡ Estado Actual — ${state.fcActual} bpm"
                ) {
                    TvLazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        val ultimas = state.lecturas.takeLast(3)
                        items(ultimas) { lectura ->
                            FcCardItem(
                                lectura = lectura,
                                onClick = { onCardClick(lectura.id) }
                            )
                        }
                    }
                }
            }

            // ── Fila 2: Historial completo ──
            item {
                RowSection(
                    title = "📋 Historial FC"
                ) {
                    TvLazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.lecturas) { lectura ->
                            FcCardItem(
                                lectura = lectura,
                                onClick = { onCardClick(lectura.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        content()
    }
}