package mx.edu.utng.tv.data.remote

import mx.edu.utng.tv.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

object NeonClient {
    private val NEON_HOST: String by lazy { BuildConfig.NEON_HOST }
    private val NEON_API_KEY: String by lazy { BuildConfig.NEON_API_KEY }
    private val NEON_DB: String by lazy { BuildConfig.NEON_DB }
    private val NEON_USER: String by lazy { BuildConfig.NEON_USER }
    private val NEON_PASSWORD: String by lazy { BuildConfig.NEON_PASSWORD }

    private val BASE_URL: String by lazy {
        "https://${NEON_HOST}/"
    }

    val AUTH_HEADER: String by lazy {
        "Bearer ${NEON_API_KEY}"
    }

    val CONN_STRING: String by lazy {
        "postgresql://${NEON_USER}:${NEON_PASSWORD}@${NEON_HOST}/${NEON_DB}?sslmode=require"
    }

    private val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<X509Certificate>?, authType: String?) {}
        override fun checkServerTrusted(chain: Array<X509Certificate>?, authType: String?) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    })

    private val sslSocketFactory: SSLSocketFactory by lazy {
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        sslContext.socketFactory
    }

    private val hostnameVerifier = HostnameVerifier { _, _ -> true }

    val api: NeonApiService by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier(hostnameVerifier)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(NeonApiService::class.java)
    }
}