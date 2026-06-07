package mx.edu.utng.prgs.smarthealthmonitor2

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.edu.utng.prgs.smarthealthmonitor2.data.SmartHealthRepository

class SmartHealthApp : Application() {
    override fun onCreate() {
        super.onCreate()

        SmartHealthRepository.init(this)

        CoroutineScope(Dispatchers.IO).launch {
            SmartHealthRepository.limpiarHistorialAntiguo()
        }
    }
}

