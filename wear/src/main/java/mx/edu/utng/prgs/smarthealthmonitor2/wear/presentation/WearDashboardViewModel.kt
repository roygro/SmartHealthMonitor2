package mx.edu.utng.prgs.smarthealthmonitor2.wear.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WearDashboardViewModel : ViewModel() {

    private val _fc = MutableStateFlow(72)
    val fc: StateFlow<Int> = _fc.asStateFlow()

    // Historial simulado (después conectará con Room)
    private val _historial = MutableStateFlow<List<LecturaFC>>(emptyList())
    val historial: StateFlow<List<LecturaFC>> = _historial.asStateFlow()

    init {
        // Simular cambios de FC cada 2 segundos
        viewModelScope.launch {
            var currentFc = 72
            while (true) {
                delay(2000)
                currentFc = if (currentFc == 72) 110 else 72
                _fc.value = currentFc

                // Solo agregar al historial cuando sea anormal (110)
                if (currentFc == 110) {
                    val nuevaLectura = LecturaFC(
                        valorBpm = currentFc
                        // id = auto-generado por Room
                        // timestamp = automático
                        // hora = automático
                        // esNormal = calculado automáticamente
                    )
                    _historial.value = listOf(nuevaLectura) + _historial.value
                }
            }
        }
    }
}