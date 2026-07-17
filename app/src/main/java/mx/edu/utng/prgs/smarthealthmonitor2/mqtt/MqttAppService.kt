package mx.edu.utng.prgs.smarthealthmonitor2.mqtt

import android.content.Context
import android.util.Log
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mx.edu.utng.prgs.smarthealthmonitor2.data.SmartHealthRepository
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.net.ssl.SSLSocketFactory

class MqttAppService(
    private val context: Context
) {
    private var client: MqttAsyncClient? = null
    private val TAG = "MQTT_APP"

    fun connect() {
        try {
            client = MqttAsyncClient(
                MqttConfig.BROKER_URL,
                MqttConfig.CLIENT_APP,
                MemoryPersistence()
            )

            val options = MqttConnectOptions().apply {
                userName = MqttConfig.USERNAME
                password = MqttConfig.PASSWORD.toCharArray()
                isCleanSession = true
                connectionTimeout = 30
                keepAliveInterval = 60
                socketFactory = SSLSocketFactory.getDefault()
            }

            client?.setCallback(object : MqttCallback {
                override fun messageArrived(topic: String, msg: MqttMessage) {
                    when (topic) {
                        MqttConfig.TOPIC_FC -> handleFcMessage(msg)
                    }
                }

                override fun connectionLost(cause: Throwable?) {
                    Log.w(TAG, "⚠️ Conexión perdida: ${cause?.message}")
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {}
            })

            client?.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(token: IMqttToken?) {
                    client?.subscribe(MqttConfig.TOPIC_FC, MqttConfig.QOS)
                    Log.d(TAG, "✅ Conectado y suscrito a ${MqttConfig.TOPIC_FC}")
                }

                override fun onFailure(token: IMqttToken?, ex: Throwable?) {
                    Log.e(TAG, "❌ Error de conexión: ${ex?.message}")
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error al conectar: ${e.message}")
        }
    }

    private fun handleFcMessage(msg: MqttMessage) {
        try {
            val fcMsg = Json.decodeFromString<FcMessage>(String(msg.payload))

            // 1. Actualizar el Repository usando el método existente
            SmartHealthRepository.actualizarFC(fcMsg.bpm)
            Log.d(TAG, "📥 Recibido del reloj: ${fcMsg.bpm} bpm ✅")

            // 2. Re-publicar al topic TV con formato enriquecido
            val hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            val tvMsg = TvMessage(
                bpm = fcMsg.bpm,
                estado = fcMsg.estado,
                hora = hora
            )

            val tvPayload = Json.encodeToString(tvMsg).toByteArray()
            val tvMqtt = MqttMessage(tvPayload).apply {
                qos = MqttConfig.QOS
                isRetained = true
            }

            client?.publish(MqttConfig.TOPIC_TV, tvMqtt)
            Log.d(TAG, "🔁 Re-publicado al TV: ${fcMsg.bpm} bpm → ${MqttConfig.TOPIC_TV}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error al procesar mensaje: ${e.message}")
        }
    }

    fun disconnect() {
        try {
            client?.disconnect()
            Log.d(TAG, "🔌 Desconectado")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error al desconectar: ${e.message}")
        }
    }
}