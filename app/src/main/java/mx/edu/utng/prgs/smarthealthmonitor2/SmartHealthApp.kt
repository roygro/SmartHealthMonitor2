package mx.edu.utng.prgs.smarthealthmonitor2

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.edu.utng.prgs.smarthealthmonitor2.data.SmartHealthRepository
import mx.edu.utng.prgs.smarthealthmonitor2.mqtt.MqttAppService

class SmartHealthApp : Application() {
    lateinit var mqttService: MqttAppService

    override fun onCreate() {
        super.onCreate()

        // Inicializar Repository
        SmartHealthRepository.init(this)

        // Limpiar historial antiguo
        CoroutineScope(Dispatchers.IO).launch {
            SmartHealthRepository.limpiarHistorialAntiguo()
        }

        // ⭐ Inicializar MQTT (ya no necesita pasar fcFlow)
        mqttService = MqttAppService(context = this)
        mqttService.connect()
    }
}