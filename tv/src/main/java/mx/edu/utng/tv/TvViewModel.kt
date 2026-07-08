package mx.edu.utng.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TvViewModel : ViewModel() {

    // FC actual (simulado)
    private val _fc = MutableStateFlow(0)
    val fc: StateFlow<Int> = _fc.asStateFlow()

    // Historial de lecturas
    private val _historial = MutableStateFlow<List<LecturaFC>>(emptyList())
    val historial: StateFlow<List<LecturaFC>> = _historial.asStateFlow()

    init {
        cargarHistorial()
    }

    fun cargarHistorial() {
        _historial.value = MockData.historialFC
    }

    fun actualizarFC(nuevoValor: Int) {
        _fc.value = nuevoValor
        val nuevaLectura = LecturaFC(
            id = _historial.value.size + 1,
            valorBpm = nuevoValor,
            hora = SimpleDateFormat("hh:mm a", Locale.getDefault())
                .format(Date()),
            esNormal = nuevoValor in 60..100
        )
        _historial.value = _historial.value + nuevaLectura
    }
}