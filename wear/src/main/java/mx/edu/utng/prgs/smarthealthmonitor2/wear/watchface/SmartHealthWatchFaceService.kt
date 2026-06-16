package mx.edu.utng.prgs.smarthealthmonitor2.wear.watchface

import android.graphics.*
import android.view.SurfaceHolder
import androidx.wear.watchface.CanvasType
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchFace
import androidx.wear.watchface.WatchFaceService
import androidx.wear.watchface.WatchFaceType
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.ComplicationSlotsManager  // ← Este import funciona con la dependencia correcta
import androidx.wear.watchface.style.CurrentUserStyleRepository
import java.time.ZonedDateTime

class SmartHealthWatchFaceService : WatchFaceService() {

    override suspend fun createWatchFace(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        complicationSlotsManager: ComplicationSlotsManager,
        currentUserStyleRepository: CurrentUserStyleRepository
    ): WatchFace {

        val renderer = MyRenderer(
            surfaceHolder = surfaceHolder,
            watchState = watchState,
            currentUserStyleRepository = currentUserStyleRepository
        )

        return WatchFace(
            watchFaceType = WatchFaceType.DIGITAL,
            renderer = renderer
        )
    }
}

class MyRenderer(
    surfaceHolder: SurfaceHolder,
    watchState: WatchState,
    currentUserStyleRepository: CurrentUserStyleRepository
) : Renderer.CanvasRenderer(
    surfaceHolder = surfaceHolder,
    currentUserStyleRepository = currentUserStyleRepository,
    watchState = watchState,
    canvasType = CanvasType.HARDWARE,
    interactiveDrawModeUpdateDelayMillis = 1000L
) {

    private val paintHora = Paint().apply {
        color = Color.WHITE
        textSize = 72f
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
        textAlign = Paint.Align.CENTER
    }

    private val paintFC = Paint().apply {
        color = Color.RED
        textSize = 30f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    private val paintSub = Paint().apply {
        color = Color.GRAY
        textSize = 22f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    override fun render(canvas: Canvas, bounds: Rect, zonedDateTime: ZonedDateTime) {
        canvas.drawColor(Color.BLACK)

        val centerX = bounds.exactCenterX()
        val centerY = bounds.exactCenterY()

        val hora = String.format("%02d:%02d",
            zonedDateTime.hour,
            zonedDateTime.minute)
        canvas.drawText(hora, centerX, centerY - 10f, paintHora)

        val segundos = String.format("%02d", zonedDateTime.second)
        canvas.drawText(segundos, centerX, centerY + 30f, paintSub)

        val fc = 72
        val fcStr = "❤️ $fc bpm"
        canvas.drawText(fcStr, centerX, centerY + 70f, paintFC)
    }

    override fun renderHighlightLayer(canvas: Canvas, bounds: Rect, zonedDateTime: ZonedDateTime) {
        canvas.drawColor(renderParameters.highlightLayer?.backgroundTint ?: Color.TRANSPARENT)
    }
}