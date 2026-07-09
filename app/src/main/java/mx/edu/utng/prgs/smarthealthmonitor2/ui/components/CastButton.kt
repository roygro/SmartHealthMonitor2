package mx.edu.utng.prgs.smarthealthmonitor2.ui.components

import android.content.Context
import android.graphics.Color
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.mediarouter.app.MediaRouteButton
import com.google.android.gms.cast.framework.CastButtonFactory

@Composable
fun CastButton(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AndroidView(
        factory = { ctx: Context ->
            MediaRouteButton(ctx).apply {
                // Configurar el botón de Cast
                CastButtonFactory.setUpMediaRouteButton(ctx, this)

                // Asegurar que es clickeable
                isEnabled = true
                isFocusable = true

                // ✅ Fondo transparente para que se vea bien
                setBackgroundColor(Color.TRANSPARENT)
            }
        },
        modifier = modifier.size(48.dp)
    )
}