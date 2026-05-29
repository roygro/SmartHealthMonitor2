package mx.edu.utng.prgs.smarthealthmonitor2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import mx.edu.utng.prgs.smarthealthmonitor2.data.models.MockData
import mx.edu.utng.prgs.smarthealthmonitor2.data.SmartHealthRepository

class DashboardViewModel : ViewModel() {

    val fc: StateFlow<Int> = SmartHealthRepository.fcFlow
        .map { if (it == 0) MockData.fcActual else it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MockData.fcActual
        )

    val pasos: StateFlow<Int> = SmartHealthRepository.pasosFlow
        .map { if (it == 0) MockData.pasosActual else it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MockData.pasosActual
        )

    // ⭐ NUEVO: SpO2
    val spO2: StateFlow<Int> = SmartHealthRepository.spO2Flow
        .map { if (it == 0) 98 else it }  // Valor normal por defecto: 98%
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 98
        )

    val historial = MockData.historialFC
}