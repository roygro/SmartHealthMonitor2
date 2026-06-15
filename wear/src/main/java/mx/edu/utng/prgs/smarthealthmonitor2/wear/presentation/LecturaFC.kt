package mx.edu.utng.prgs.smarthealthmonitor2.wear.presentation

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class LecturaFC(
    val id: Int = 0,
    val valorBpm: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val hora: String = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
    val esNormal: Boolean = valorBpm in 60..100
)