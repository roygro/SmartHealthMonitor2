package mx.edu.utng.prgs.smarthealthmonitor2.data.sync

import android.content.Context
import android.util.Log
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.edu.utng.prgs.smarthealthmonitor2.data.db.SmartHealthDatabase
import mx.edu.utng.prgs.smarthealthmonitor2.data.repository.SyncRepository
import java.util.concurrent.TimeUnit

class NeonSyncWorker(
    ctx: Context,
    params: WorkerParameters
) : CoroutineWorker(ctx, params) {

    private val TAG = "SYNC_WORKER"

    override suspend fun doWork(): Result {
        return try {
            val db = SmartHealthDatabase.getDatabase(applicationContext)
            val repo = SyncRepository(db.lecturaDao())

            // 1. Enviar pendientes locales a Neon
            Log.d(TAG, "📤 Enviando pendientes locales...")
            repo.enviarPendientes()

            // 2. Descargar los más recientes de Neon
            Log.d(TAG, "📥 Descargando datos de Neon...")
            repo.sincronizarDesdeNeon(limite = 100)

            Log.d(TAG, "✅ Sync completado exitosamente")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "❌ Sync fallido: ${e.message}")
            Result.retry()  // WorkManager reintentará automáticamente
        }
    }

    companion object {
        const val WORK_NAME = "NeonSyncWork"

        fun schedule(context: Context) {
            Log.d("SYNC_WORKER", "📅 Programando sync periódico...")

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = PeriodicWorkRequestBuilder<NeonSyncWorker>(
                30, TimeUnit.MINUTES  // Cada 30 minutos
            )
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    5, TimeUnit.MINUTES
                )
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )

            Log.d("SYNC_WORKER", "✅ Sync programado cada 30 minutos")
        }

        // Para ejecutar sync manual (debug)
        fun startOneTimeSync(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = OneTimeWorkRequestBuilder<NeonSyncWorker>()
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueue(request)
        }
    }
}