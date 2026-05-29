package mx.edu.utng.prgs.smarthealthmonitor2.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SmartHealthRepository {

    // FC actual del wearable (bpm)
    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    // Pasos del día actual
    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    // ⭐ NUEVO: SpO2 (saturación de oxígeno, %)
    private val _spO2Flow = MutableStateFlow(0)
    val spO2Flow: StateFlow<Int> = _spO2Flow.asStateFlow()

    fun actualizarFC(bpm: Int) {
        _fcFlow.value = bpm
    }

    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }

    // ⭐ NUEVO
    fun actualizarSpO2(spO2: Int) {
        _spO2Flow.value = spO2
    }
}