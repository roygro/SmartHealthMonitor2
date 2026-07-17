package mx.edu.utng.tv.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mx.edu.utng.tv.data.remote.LecturaFcDto
import mx.edu.utng.tv.data.remote.NeonClient
import mx.edu.utng.tv.data.remote.NeonRequest

class TvNeonRepository {
    private val TAG = "TV_NEON"

    suspend fun obtenerHistorialCompleto(limite: Int = 50): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            try {
                val response = NeonClient.api.executeQuery(
                    auth = NeonClient.AUTH_HEADER,
                    connStr = NeonClient.CONN_STRING,
                    request = NeonRequest(
                        query = """
                            SELECT id, bpm, estado, dispositivo, hora, fecha, created_at
                            FROM lecturas_fc
                            ORDER BY created_at DESC
                            LIMIT $1
                        """.trimIndent(),
                        params = listOf(limite)
                    )
                )
                Log.d(TAG, "📥 ${response.rowCount} registros descargados de Neon")
                response.rows
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error al obtener historial: ${e.message}")
                emptyList()
            }
        }

    suspend fun obtenerEstadisticas(): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            try {
                val response = NeonClient.api.executeQuery(
                    auth = NeonClient.AUTH_HEADER,
                    connStr = NeonClient.CONN_STRING,
                    request = NeonRequest(
                        query = """
                            SELECT 
                                dispositivo,
                                ROUND(AVG(bpm)) AS bpm,
                                'Promedio' AS estado,
                                MAX(hora) AS hora
                            FROM lecturas_fc
                            GROUP BY dispositivo
                        """.trimIndent()
                    )
                )
                response.rows
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error al obtener estadísticas: ${e.message}")
                emptyList()
            }
        }

    suspend fun obtenerAlertasRecientes(): List<LecturaFcDto> =
        withContext(Dispatchers.IO) {
            try {
                val response = NeonClient.api.executeQuery(
                    auth = NeonClient.AUTH_HEADER,
                    connStr = NeonClient.CONN_STRING,
                    request = NeonRequest(
                        query = """
                            SELECT id, bpm, estado, dispositivo, hora, fecha, created_at
                            FROM lecturas_fc
                            WHERE (bpm < 60 OR bpm > 100)
                            ORDER BY created_at DESC
                            LIMIT 10
                        """.trimIndent()
                    )
                )
                response.rows
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error al obtener alertas: ${e.message}")
                emptyList()
            }
        }
}