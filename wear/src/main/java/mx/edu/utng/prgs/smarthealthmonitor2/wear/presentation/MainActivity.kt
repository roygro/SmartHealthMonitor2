package mx.edu.utng.prgs.smarthealthmonitor2.wear.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import mx.edu.utng.prgs.smarthealthmonitor2.wear.presentation.theme.SmartHealthWearTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartHealthWearTheme {
                // TODO Ej.02: reemplazar con WearNavGraph
                // Pantalla en blanco/gris como pide la guía
            }
        }
    }
}