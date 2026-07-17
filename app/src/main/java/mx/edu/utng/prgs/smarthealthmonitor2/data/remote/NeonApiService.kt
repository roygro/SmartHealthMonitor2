package mx.edu.utng.prgs.smarthealthmonitor2.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.*

// Request genérico para la Neon HTTP API
data class NeonRequest(
    val query: String,
    val params: List<Any> = emptyList()
)

// Response de la Neon HTTP API
data class NeonResponse<T>(
    val rows: List<T> = emptyList(),
    val rowCount: Int = 0,
    val command: String = ""
)

// DTO de lectura FC (mapea fila de PostgreSQL)
data class LecturaFcDto(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("bpm")
    val bpm: Int,

    @SerializedName("estado")
    val estado: String,

    @SerializedName("dispositivo")
    val dispositivo: String,

    @SerializedName("hora")
    val hora: String,

    @SerializedName("fecha")
    val fecha: String = "",

    @SerializedName("created_at")
    val created_at: String = ""
)

// Interfaz Retrofit para la Neon HTTP API
interface NeonApiService {
    @POST("sql")
    suspend fun executeQuery(
        @Header("Authorization") auth: String,
        @Header("Neon-Connection-String") connStr: String,
        @Body request: NeonRequest
    ): NeonResponse<LecturaFcDto>
}