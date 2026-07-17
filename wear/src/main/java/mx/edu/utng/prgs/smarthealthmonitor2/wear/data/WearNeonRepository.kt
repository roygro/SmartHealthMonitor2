package mx.edu.utng.prgs.smarthealthmonitor2.wear.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mx.edu.utng.prgs.smarthealthmonitor2.data.remote.LecturaFcDto
import mx.edu.utng.prgs.smarthealthmonitor2.data.remote.NeonClient
import mx.edu.utng.prgs.smarthealthmonitor2.data.remote.NeonRequest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WearNeonRepository {
    private val TAG = "WEAR_NEON"

    suspend fun publicarLectura(bpm: Int, estado: String) =
        withContext(Dispatchers.IO) {
            try {
                val hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

                NeonClient.api.executeQuery(
                    auth = NeonClient.AUTH_HEADER,
                    connStr = NeonClient.CONN_STRING,
                    request = NeonRequest(
                        query = """
                            INSERT INTO lecturas_fc (bpm, estado, dispositivo, hora)
                            VALUES ($1, $2, $3, $4)
                        """.trimIndent(),
                        params = listOf(bpm, estado, "wear", hora)
                    )
                )
                Log.d(TAG, "⌚ FC enviada a Neon: $bpm bpm")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error al enviar a Neon: ${e.message}")
            }
        }

    suspend fun obtenerUltimasLecturas(limite: Int = 5): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            try {
                val response = NeonClient.api.executeQuery(
                    auth = NeonClient.AUTH_HEADER,
                    connStr = NeonClient.CONN_STRING,
                    request = NeonRequest(
                        query = """
                            SELECT id, bpm, estado, dispositivo, hora, fecha, created_at
                            FROM lecturas_fc
                            WHERE dispositivo = 'wear'
                            ORDER BY created_at DESC
                            LIMIT $1
                        """.trimIndent(),
                        params = listOf(limite)
                    )
                )
                response.rows
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error al obtener lecturas: ${e.message}")
                emptyList()
            }
        }
}