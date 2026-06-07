package mx.edu.utng.prgs.smarthealthmonitor2.wear.presentation.wear

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.tasks.await

class WearDataSender(private val context: Context) {

    companion object {
        private const val TAG = "WearDataSender"
        const val PATH_FC    = "/smarthealthmonitor/fc"
        const val PATH_PASOS = "/smarthealthmonitor/pasos"
        const val PATH_SPO2  = "/smarthealthmonitor/spo2"
    }

    /** Obtiene el nodeId del teléfono emparejado (null si no hay ninguno). */
    private suspend fun phoneNodeId(): String? {
        return try {
            Wearable.getNodeClient(context)
                .connectedNodes.await()
                .firstOrNull()?.id
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo nodo del teléfono: ${e.message}")
            null
        }
    }

    /** Envía la frecuencia cardíaca al teléfono. */
    suspend fun enviarFC(bpm: Int) = enviar(PATH_FC, bpm.toString())

    /** Envía el conteo de pasos diarios al teléfono. */
    suspend fun enviarPasos(pasos: Long) = enviar(PATH_PASOS, pasos.toString())

    /** Envía la saturación de oxígeno (SpO2) al teléfono. */
    suspend fun enviarSpO2(spO2: Int) = enviar(PATH_SPO2, spO2.toString())

    private suspend fun enviar(path: String, payload: String) {
        val nodeId = phoneNodeId() ?: run {
            Log.w(TAG, "No hay teléfono conectado. Path=$path, payload=$payload")
            return
        }
        try {
            Wearable.getMessageClient(context)
                .sendMessage(nodeId, path, payload.toByteArray())
                .await()
            Log.d(TAG, "Enviado → path=$path  payload=$payload  nodo=$nodeId")
        } catch (e: Exception) {
            Log.e(TAG, "Error enviando mensaje: ${e.message}")
        }
    }
}
