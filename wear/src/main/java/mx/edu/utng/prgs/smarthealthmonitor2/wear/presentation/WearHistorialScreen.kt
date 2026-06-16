package mx.edu.utng.prgs.smarthealthmonitor2.wear.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rotary.rotaryScrollable
import androidx.wear.compose.foundation.rotary.RotaryScrollableDefaults
import androidx.wear.compose.material.*
import mx.edu.utng.prgs.smarthealthmonitor2.wear.presentation.components.WearFilaHistorial

@Composable
fun WearHistorialScreen(
    onBack: () -> Unit,
    viewModel: WearDashboardViewModel = viewModel()
) {
    val historial by viewModel.historial.collectAsState()
    val listState = rememberScalingLazyListState()
    val focusRequester = remember { FocusRequester() }

    // Pedir foco para recibir eventos de la corona
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        timeText = {
            TimeText(modifier = Modifier.scrollAway(listState))
        },
        positionIndicator = {
            PositionIndicator(scalingLazyListState = listState)
        }
    ) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .rotaryScrollable(
                    behavior = RotaryScrollableDefaults.behavior(
                        scrollableState = listState
                    ),
                    focusRequester = focusRequester
                )
        ) {
            item {
                Text(
                    text = "Historial (${historial.size})",
                    style = MaterialTheme.typography.title3,
                    modifier = Modifier.padding(8.dp)
                )
            }
            if (historial.isEmpty()) {
                item {
                    Text(
                        text = "Sin lecturas aún",
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            } else {
                items(historial, key = { it.id }) { lectura ->
                    WearFilaHistorial(lectura = lectura)
                }
            }
        }
    }
}