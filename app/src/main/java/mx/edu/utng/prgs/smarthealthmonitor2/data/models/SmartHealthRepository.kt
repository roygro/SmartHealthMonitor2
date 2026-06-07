package mx.edu.utng.prgs.smarthealthmonitor2.data

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import mx.edu.utng.prgs.smarthealthmonitor2.data.db.LecturaFC
import mx.edu.utng.prgs.smarthealthmonitor2.data.db.LecturaFCDao
import mx.edu.utng.prgs.smarthealthmonitor2.data.db.SmartHealthDB

object SmartHealthRepository {

    // Scope propio para operaciones de Room (evita GlobalScope)
    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // FC actual del wearable (bpm)
    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    // Pasos del día actual
    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    // SpO2 (saturación de oxígeno, %)
    private val _spO2Flow = MutableStateFlow(0)
    val spO2Flow: StateFlow<Int> = _spO2Flow.asStateFlow()

    // Room DAO
    private var dao: LecturaFCDao? = null

    // Inicializar Room (llamar desde Application.onCreate)
    fun init(context: Context) {
        dao = SmartHealthDB.getDatabase(context).lecturaDao()
    }

    fun actualizarFC(bpm: Int) {
        _fcFlow.value = bpm
        // Persistir en Room automáticamente
        repositoryScope.launch {
            dao?.insertar(LecturaFC(valorBpm = bpm))
        }
    }

    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }

    // ⭐ Reto: sobrecarga que acepta Long (desde el Wear OS)
    fun actualizarPasosLong(pasos: Long) {
        _pasosFlow.value = pasos.coerceAtMost(Int.MAX_VALUE.toLong()).toInt()
    }

    fun actualizarSpO2(spO2: Int) {
        _spO2Flow.value = spO2
    }

    // Obtener historial desde Room (Flow reactivo)
    fun obtenerHistorial(): Flow<List<LecturaFC>> =
        dao?.obtenerUltimas() ?: emptyFlow()

    /**
     * Limpiar historial antiguo.
     * @param limiteMs timestamp en ms antes del cual se borran las lecturas.
     *                 Por defecto: 7 días atrás.
     */
    suspend fun limpiarHistorialAntiguo(
        limiteMs: Long = System.currentTimeMillis() - (7L * 24 * 60 * 60 * 1000)
    ) {
        dao?.limpiarViejos(limiteMs)
    }
}