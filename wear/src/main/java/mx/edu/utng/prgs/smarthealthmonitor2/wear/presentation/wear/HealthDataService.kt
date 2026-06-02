package mx.edu.utng.prgs.smarthealthmonitor2.wear.presentation.wear

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

class HealthDataService : Service() {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    companion object {
        private const val TAG = "HealthDataService"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "HealthDataService onCreate - Servicio de salud iniciado")

        // Iniciar monitoreo simulado
        startMonitoring()
    }

    private fun startMonitoring() {
        scope.launch {
            while (true) {
                delay(5000) // Cada 5 segundos
                val bpmSimulado = (60..100).random()
                Log.d(TAG, "FC simulada: $bpmSimulado bpm")
                // TODO: Enviar al teléfono cuando implementemos MessageClient
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "HealthDataService onStartCommand")
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        Log.d(TAG, "HealthDataService onDestroy")
    }
}