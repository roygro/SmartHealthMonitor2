package mx.edu.utng.tv.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NeonClient {
    private const val NEON_HOST = "ep-crimson-brook-awb1em3b-pooler.c-12.us-east-1.aws.neon.tech"
    private const val NEON_API_KEY = "napi_w0e2bhzobvud6pm85vow5qj7wj5h5ujfs7v3ylahdo89v7ajq69f8n0pjw74xpsu"
    private const val NEON_DB = "neondb"
    private const val NEON_USER = "neondb_owner"
    private const val NEON_PASSWORD = "npg_83YqQPSCNkMK"

    private val BASE_URL: String by lazy {
        "https://${NEON_HOST}/"
    }

    val AUTH_HEADER: String by lazy {
        "Bearer ${NEON_API_KEY}"
    }

    val CONN_STRING: String by lazy {
        "postgresql://${NEON_USER}:${NEON_PASSWORD}@${NEON_HOST}/${NEON_DB}?sslmode=require"
    }

    val api: NeonApiService by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(NeonApiService::class.java)
    }
}