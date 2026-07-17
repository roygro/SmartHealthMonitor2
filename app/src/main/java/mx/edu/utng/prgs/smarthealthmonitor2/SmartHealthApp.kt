package mx.edu.utng.prgs.smarthealthmonitor2

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.edu.utng.prgs.smarthealthmonitor2.data.SmartHealthRepository
import mx.edu.utng.prgs.smarthealthmonitor2.data.sync.NeonSyncWorker
import mx.edu.utng.prgs.smarthealthmonitor2.mqtt.MqttAppService

class SmartHealthApp : Application() {
    lateinit var mqttService: MqttAppService

    override fun onCreate() {
        super.onCreate()

        SmartHealthRepository.init(this)

        CoroutineScope(Dispatchers.IO).launch {
            SmartHealthRepository.limpiarHistorialAntiguo()
        }

        // ⭐ Inicializar MQTT (ahora sin parámetros)
        mqttService = MqttAppService(context = this)
        mqttService.connect()

        // Programar sync periódico con Neon
        NeonSyncWorker.schedule(this)
    }
}