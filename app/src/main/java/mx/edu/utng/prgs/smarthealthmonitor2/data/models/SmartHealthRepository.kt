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
import mx.edu.utng.prgs.smarthealthmonitor2.data.db.SmartHealthDatabase  // ← CAMBIAR

object SmartHealthRepository {

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    private val _spO2Flow = MutableStateFlow(0)
    val spO2Flow: StateFlow<Int> = _spO2Flow.asStateFlow()

    private var dao: LecturaFCDao? = null

    fun init(context: Context) {
        dao = SmartHealthDatabase.getDatabase(context).lecturaDao()  // ← CAMBIAR
    }

    fun actualizarFC(bpm: Int) {
        _fcFlow.value = bpm
        repositoryScope.launch {
            dao?.insertar(LecturaFC(valorBpm = bpm))
        }
    }

    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }

    fun actualizarPasosLong(pasos: Long) {
        _pasosFlow.value = pasos.coerceAtMost(Int.MAX_VALUE.toLong()).toInt()
    }

    fun actualizarSpO2(spO2: Int) {
        _spO2Flow.value = spO2
    }

    fun obtenerHistorial(): Flow<List<LecturaFC>> =
        dao?.obtenerUltimas() ?: emptyFlow()

    suspend fun limpiarHistorialAntiguo(
        limiteMs: Long = System.currentTimeMillis() - (7L * 24 * 60 * 60 * 1000)
    ) {
        dao?.limpiarViejos(limiteMs)
    }
}