package mx.edu.utng.tv

import kotlinx.coroutines.*
import kotlin.random.Random

object DataSimulator {
    private var job: Job? = null

    fun startSimulation(viewModel: TvViewModel) {
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
            // Cargar historial inicial
            viewModel.cargarHistorial()

            // Simular nuevas lecturas cada 5 segundos
            while (isActive) {
                delay(5000)
                val nuevoBpm = Random.nextInt(55, 130)
                viewModel.actualizarFC(nuevoBpm)
            }
        }
    }

    fun stopSimulation() {
        job?.cancel()
        job = null
    }
}