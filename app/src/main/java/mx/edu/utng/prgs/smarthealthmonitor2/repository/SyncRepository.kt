package mx.edu.utng.prgs.smarthealthmonitor2.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import mx.edu.utng.prgs.smarthealthmonitor2.data.db.LecturaFC
import mx.edu.utng.prgs.smarthealthmonitor2.data.db.LecturaFCDao
import mx.edu.utng.prgs.smarthealthmonitor2.data.remote.NeonClient
import mx.edu.utng.prgs.smarthealthmonitor2.data.remote.NeonRequest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SyncRepository(
    private val dao: LecturaFCDao
) {
    private val TAG = "SYNC_REPO"

    fun observarHistorial(): Flow<List<LecturaFC>> = dao.obtenerUltimas()

    suspend fun insertarLectura(lectura: LecturaFC) {
        // 1. Guardar localmente PRIMERO (nunca falla)
        val id = dao.insertar(lectura)  // ← AHORA devuelve Long
        Log.d(TAG, "📝 Guardado local: ${lectura.valorBpm} bpm (id=$id)")

        // 2. Intentar sync con Neon (puede fallar sin internet)
        try {
            sincronizarHaciaNeon(lectura)
            dao.marcarSincronizado(id)  // ← id es Long
            Log.d(TAG, "✅ Sincronizado con Neon: ${lectura.valorBpm} bpm")
        } catch (e: Exception) {
            Log.w(TAG, "⚠️ Pendiente de sync: ${e.message}")
        }
    }

    // ── PUSH: Room → Neon ──────────────────────────────────
    private suspend fun sincronizarHaciaNeon(lectura: LecturaFC) =
        withContext(Dispatchers.IO) {
            val estado = if (lectura.esNormal) "Normal" else "Alerta"
            val hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

            NeonClient.api.executeQuery(
                auth = NeonClient.AUTH_HEADER,
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(
                    query = """
                        INSERT INTO lecturas_fc (bpm, estado, dispositivo, hora)
                        VALUES ($1, $2, $3, $4)
                    """.trimIndent(),
                    params = listOf(
                        lectura.valorBpm,
                        estado,
                        "app",
                        hora
                    )
                )
            )
            Log.d(TAG, "📤 Sincronizado a Neon: ${lectura.valorBpm} bpm")
        }

    // ── PULL: Neon → Room ──────────────────────────────────
    suspend fun sincronizarDesdeNeon(limite: Int = 50) =
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

                response.rows.forEach { dto ->
                    dao.upsert(
                        LecturaFC(
                            id = dto.id,
                            valorBpm = dto.bpm,
                            hora = dto.hora,
                            esNormal = dto.estado == "Normal",
                            sincronizado = true
                        )
                    )
                }

                Log.d(TAG, "📥 ${response.rowCount} registros descargados de Neon")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error al descargar de Neon: ${e.message}")
            }
        }

    // ── Sincronizar pendientes ──────────────────────────────
    suspend fun enviarPendientes() = withContext(Dispatchers.IO) {
        val pendientes = dao.obtenerNoSincronizados()
        if (pendientes.isEmpty()) {
            Log.d(TAG, "✅ No hay pendientes de sync")
            return@withContext
        }

        Log.d(TAG, "📤 Enviando ${pendientes.size} pendientes...")

        pendientes.forEach { lectura ->
            try {
                sincronizarHaciaNeon(lectura)
                dao.marcarSincronizado(lectura.id.toLong())
                Log.d(TAG, "✅ Sincronizado pendiente id=${lectura.id}")
            } catch (e: Exception) {
                Log.w(TAG, "⚠️ Aún sin internet: ${e.message}")
            }
        }
    }

    suspend fun contarPendientes(): Int = withContext(Dispatchers.IO) {
        return@withContext dao.contarPendientes()
    }
}