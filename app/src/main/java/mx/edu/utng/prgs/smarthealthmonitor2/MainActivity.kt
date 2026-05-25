package mx.edu.utng.prgs.smarthealthmonitor2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import mx.edu.utng.prgs.smarthealthmonitor2.ui.theme.SmartHealthMonitor2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartHealthMonitor2Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LoginScreen(
                        onLoginSuccess = {
                            Log.d("SmartHealth2", "Login exitoso")
                            // TODO sesión 5: navegar al Dashboard
                        }
                    )
                }
            }
        }
    }
}