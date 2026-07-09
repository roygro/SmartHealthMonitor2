package mx.edu.utng.tv.domain.model

import mx.edu.utng.tv.LecturaFC

data class TvUiState(
    val lecturas: List<LecturaFC> = emptyList(),
    val fcActual: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null
)