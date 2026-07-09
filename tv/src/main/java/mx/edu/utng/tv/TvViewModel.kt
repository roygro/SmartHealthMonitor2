package mx.edu.utng.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.edu.utng.tv.domain.model.TvUiState

class TvViewModel : ViewModel() {

    private val _state = MutableStateFlow(TvUiState())
    val state: StateFlow<TvUiState> = _state.asStateFlow()

    init {
        cargarDatos()
    }

    fun cargarDatos() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            // Usar MockData para simular
            val lecturas = MockData.historialFC
            val fcActual = lecturas.lastOrNull()?.valorBpm ?: 0

            _state.update {
                it.copy(
                    lecturas = lecturas,
                    fcActual = fcActual,
                    isLoading = false
                )
            }
        }
    }

    fun actualizarFC(nuevoValor: Int) {
        val nuevaLectura = LecturaFC(
            id = _state.value.lecturas.size + 1,
            valorBpm = nuevoValor,
            hora = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())
                .format(java.util.Date()),
            esNormal = nuevoValor in 60..100
        )
        _state.update {
            it.copy(
                lecturas = it.lecturas + nuevaLectura,
                fcActual = nuevoValor
            )
        }
    }
}