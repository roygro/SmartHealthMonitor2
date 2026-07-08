package mx.edu.utng.tv

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : FragmentActivity() {

    private lateinit var viewModel: TvViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar ViewModel manualmente
        viewModel = ViewModelProvider(this).get(TvViewModel::class.java)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_browse_fragment, MainFragment())
                .commit()
        }

        // Iniciar simulación de datos
        DataSimulator.startSimulation(viewModel)
    }

    override fun onDestroy() {
        super.onDestroy()
        DataSimulator.stopSimulation()
    }
}