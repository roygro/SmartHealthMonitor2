package mx.edu.utng.prgs.smarthealthmonitor2.wear.presentation.wear

import android.content.Context
import android.util.Log
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveListenerConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.guava.await          // ← ListenableFuture (Health Services)
import kotlinx.coroutines.launch

class HealthDataService : PassiveListenerService() {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var wearDataSender: WearDataSender

    companion object {
        private const val TAG = "HealthDataService"

        /** Registra el PassiveListenerService con Health Services API. */
        suspend fun registrar(context: Context) {
            try {
                val hsClient     = HealthServices.getClient(context)
                val passiveClient = hsClient.passiveMonitoringClient

                val config = PassiveListenerConfig.builder()
                    .setDataTypes(
                        setOf(
                            DataType.HEART_RATE_BPM,
                            DataType.STEPS_DAILY          // ⭐ Reto: pasos diarios
                        )
                    )
                    .setShouldUserActivityInfoBeRequested(true)
                    .build()

                passiveClient.setPassiveListenerServiceAsync(
                    HealthDataService::class.java,
                    config
                ).await()

                Log.d(TAG, "PassiveListenerService registrado correctamente")
            } catch (e: Exception) {
                Log.e(TAG, "Error al registrar PassiveListenerService: ${e.message}")
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        wearDataSender = WearDataSender(this)
        Log.d(TAG, "HealthDataService creado")
    }

    override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
        // ── Frecuencia cardíaca ─────────────────────────────────────────────
        dataPoints.getData(DataType.HEART_RATE_BPM).forEach { dataPoint ->
            val bpm = (dataPoint.value as Double).toInt()
            Log.d(TAG, "FC recibida del sensor: $bpm bpm")
            scope.launch { wearDataSender.enviarFC(bpm) }
        }

        // ── Pasos diarios ⭐ Reto ───────────────────────────────────────────
        dataPoints.getData(DataType.STEPS_DAILY).forEach { dataPoint ->
            val pasos = dataPoint.value as Long
            Log.d(TAG, "Pasos diarios recibidos: $pasos")
            scope.launch { wearDataSender.enviarPasos(pasos) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        Log.d(TAG, "HealthDataService destruido")
    }
}