package mx.edu.utng.prgs.smarthealthmonitor2.data.remote

import mx.edu.utng.prgs.smarthealthmonitor2.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NeonClient {
    private val BASE_URL: String by lazy {
        "https://${BuildConfig.NEON_HOST}/"
    }

    val AUTH_HEADER: String by lazy {
        "Bearer ${BuildConfig.NEON_API_KEY}"
    }

    val CONN_STRING: String by lazy {
        "postgresql://${BuildConfig.NEON_USER}:${BuildConfig.NEON_PASSWORD}@${BuildConfig.NEON_HOST}/${BuildConfig.NEON_DB}?sslmode=require"
    }

    val api: NeonApiService by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(NeonApiService::class.java)
    }
}