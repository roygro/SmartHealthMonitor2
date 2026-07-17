package mx.edu.utng.tv.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.*

data class NeonRequest(
    val query: String,
    val params: List<Any> = emptyList()
)

data class NeonResponse<T>(
    val rows: List<T> = emptyList(),
    val rowCount: Int = 0,
    val command: String = ""
)

data class LecturaFcDto(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("bpm") val bpm: Int,
    @SerializedName("estado") val estado: String,
    @SerializedName("dispositivo") val dispositivo: String,
    @SerializedName("hora") val hora: String,
    @SerializedName("fecha") val fecha: String = "",
    @SerializedName("created_at") val created_at: String = ""
)

interface NeonApiService {
    // ⭐ Cambiar a "sql" (sin /api/v2/)
    @POST("sql")
    suspend fun executeQuery(
        @Header("Authorization") auth: String,
        @Header("Neon-Connection-String") connStr: String,
        @Body request: NeonRequest
    ): NeonResponse<LecturaFcDto>
}