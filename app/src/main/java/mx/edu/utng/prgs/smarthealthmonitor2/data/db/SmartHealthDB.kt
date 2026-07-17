package mx.edu.utng.prgs.smarthealthmonitor2.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [LecturaFC::class],
    version = 2,  // ← CAMBIAR de 1 a 2
    exportSchema = false
)
abstract class SmartHealthDatabase : RoomDatabase() {
    abstract fun lecturaDao(): LecturaFCDao

    companion object {
        @Volatile
        private var INSTANCE: SmartHealthDatabase? = null

        // ✅ Migración de versión 1 a 2
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE lecturas_fc ADD COLUMN sincronizado INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getDatabase(context: Context): SmartHealthDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SmartHealthDatabase::class.java,
                    "smarthealth_db"
                )
                    .addMigrations(MIGRATION_1_2)  // ← AGREGAR MIGRACIÓN
                    .fallbackToDestructiveMigration() // ← SOLO EN DESARROLLO
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}