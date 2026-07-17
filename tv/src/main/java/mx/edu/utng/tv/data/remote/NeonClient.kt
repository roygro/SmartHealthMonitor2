package mx.edu.utng.tv.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

object NeonClient {
    // ⭐ Usar el HOST correcto (sin -pooler)
    private const val NEON_HOST = "ep-crimson-brook-awb1em3b.c-12.us-east-1.aws.neon.tech"
    private const val NEON_API_KEY = "napi_w0e2bhzobvud6pm85vow5qj7wj5h5ujfs7v3ylahdo89v7ajq69f8n0pjw74xpsu"
    private const val NEON_DB = "neondb"
    private const val NEON_USER = "neondb_owner"
    private const val NEON_PASSWORD = "npg_83YqQPSCNkMK"

    // ⭐ Usar la URL directa del host
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