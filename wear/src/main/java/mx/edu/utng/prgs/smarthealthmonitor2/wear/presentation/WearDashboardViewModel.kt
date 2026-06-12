package mx.edu.utng.prgs.smarthealthmonitor2.wear.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WearDashboardViewModel : ViewModel() {

    // Simulación de FC (en la vida real vendría de Health Services)
    private val _fc = MutableStateFlow(72)
    val fc: StateFlow<Int> = _fc.asStateFlow()

    init {
        // Simular cambios de FC cada 2 segundos (para prueba)
        viewModelScope.launch {
            var currentFc = 72
            while (true) {
                kotlinx.coroutines.delay(2000)
                // Cambia entre valores normales y altos para probar colores
                currentFc = if (currentFc == 72) 110 else 72
                _fc.value = currentFc
            }
        }
    }
}