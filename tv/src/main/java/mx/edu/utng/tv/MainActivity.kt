package mx.edu.utng.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import mx.edu.utng.tv.presentation.TvCatalogScreen
import mx.edu.utng.tv.presentation.TvDetailScreen
import mx.edu.utng.tv.presentation.TvPlaybackScreen
import mx.edu.utng.tv.ui.theme.SmartHealthMonitor2Theme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartHealthMonitor2Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "catalog"
                    ) {
                        composable("catalog") {
                            TvCatalogScreen(
                                onCardClick = { lecturaId ->
                                    navController.navigate("detail/$lecturaId")
                                }
                            )
                        }

                        composable(
                            route = "detail/{lecturaId}",
                            arguments = listOf(
                                navArgument("lecturaId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("lecturaId") ?: 0
                            TvDetailScreen(
                                lecturaId = id,
                                navController = navController
                            )
                        }

                        composable("playback") {
                            TvPlaybackScreen(
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}