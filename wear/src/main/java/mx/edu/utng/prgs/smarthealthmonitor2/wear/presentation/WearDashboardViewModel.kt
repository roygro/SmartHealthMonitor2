package mx.edu.utng.prgs.smarthealthmonitor2.wear.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.edu.utng.prgs.smarthealthmonitor2.wear.mqtt.MqttWearPublisher  // ← IMPORT CORRECTO

class WearDashboardViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val _fc = MutableStateFlow(72)
    val fc: StateFlow<Int> = _fc.asStateFlow()

    // Historial simulado (después conectará con Room)
    private val _historial = MutableStateFlow<List<LecturaFC>>(emptyList())
    val historial: StateFlow<List<LecturaFC>> = _historial.asStateFlow()

    // MQTT Publisher
    private val mqttPublisher = MqttWearPublisher(application, viewModelScope)

    init {
        // Conectar MQTT
        mqttPublisher.connect()

        // Simular cambios de FC cada 2 segundos
        viewModelScope.launch {
            var currentFc = 72
            while (true) {
                delay(2000)
                currentFc = if (currentFc == 72) 110 else 72
                _fc.value = currentFc

                // Publicar FC vía MQTT
                val estado = when {
                    currentFc < 60 -> "FC Baja"
                    currentFc > 100 -> "FC Alta"
                    else -> "Normal"
                }
                mqttPublisher.publishFC(currentFc, estado)

                // Solo agregar al historial cuando sea anormal (110)
                if (currentFc == 110) {
                    val nuevaLectura = LecturaFC(
                        valorBpm = currentFc
                    )
                    _historial.value = listOf(nuevaLectura) + _historial.value
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mqttPublisher.disconnect()
    }
}