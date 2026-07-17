package mx.edu.utng.tv.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.edu.utng.tv.data.TvNeonRepository
import mx.edu.utng.tv.domain.model.TvUiState
import mx.edu.utng.tv.LecturaFC

class TvViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val neonRepo = TvNeonRepository()

    private val _state = MutableStateFlow(TvUiState())
    val state: StateFlow<TvUiState> = _state.asStateFlow()

    init {
        cargarDatos()
    }

    fun cargarDatos() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val lecturas = neonRepo.obtenerHistorialCompleto(50)
                val estadisticas = neonRepo.obtenerEstadisticas()
                val alertas = neonRepo.obtenerAlertasRecientes()

                _state.update {
                    it.copy(
                        lecturas = lecturas.map { dto ->
                            LecturaFC(
                                id = dto.id,
                                valorBpm = dto.bpm,
                                hora = dto.hora,
                                esNormal = dto.estado == "Normal"
                            )
                        },
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun refresh() = cargarDatos()
}