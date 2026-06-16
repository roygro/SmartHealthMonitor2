package mx.edu.utng.prgs.smarthealthmonitor2.wear.presentation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController

object WearScreens {
    const val DASHBOARD = "wear_dashboard"
    const val ALERTA = "wear_alerta"
    const val HISTORIAL = "wear_historial"
}

@Composable
fun SmartHealthWearNavGraph() {
    val navController = rememberSwipeDismissableNavController()

    SwipeDismissableNavHost(
        navController = navController,
        startDestination = WearScreens.DASHBOARD
    ) {
        // ✅ UN SOLO DASHBOARD (con ambos onClicks)
        composable(WearScreens.DASHBOARD) {
            WearDashboardScreen(
                onAlertClick = {
                    navController.navigate(WearScreens.ALERTA)
                },
                onHistorialClick = {
                    navController.navigate(WearScreens.HISTORIAL)
                }
            )
        }

        composable(WearScreens.ALERTA) {
            val vm: WearDashboardViewModel = viewModel()
            val fc by vm.fc.collectAsState()
            WearAlertaScreen(
                fc = fc,
                onConfirmar = { navController.popBackStack() },
                onCancelar = { navController.popBackStack() }
            )
        }

        // ✅ AGREGAR EL DESTINO HISTORIAL
        composable(WearScreens.HISTORIAL) {
            WearHistorialScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}