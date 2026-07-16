package mx.edu.utng.tv.mqtt

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mx.edu.utng.tv.mqtt.MqttConfig
import mx.edu.utng.tv.mqtt.TvMessage
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import javax.net.ssl.SSLSocketFactory

class MqttTvSubscriber(
    private val context: Context,
    private val tvFlow: MutableStateFlow<TvMessage?>
) {
    private var client: MqttAsyncClient? = null
    private val TAG = "MQTT_TV"

    fun connect() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                client = MqttAsyncClient(
                    MqttConfig.BROKER_URL,
                    MqttConfig.CLIENT_TV,
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
                        if (topic == MqttConfig.TOPIC_TV) {
                            try {
                                val tvMsg = Json.decodeFromString<TvMessage>(String(msg.payload))
                                tvFlow.value = tvMsg
                                Log.d(TAG, "📺 Recibido: ${tvMsg.bpm} bpm")
                            } catch (e: Exception) {
                                Log.e(TAG, "❌ Error al decodificar mensaje: ${e.message}")
                            }
                        }
                    }

                    override fun connectionLost(cause: Throwable?) {
                        Log.w(TAG, "⚠️ Conexión perdida: ${cause?.message}")
                    }

                    override fun deliveryComplete(token: IMqttDeliveryToken?) {
                        // No usado
                    }
                })

                client?.connect(options, null, object : IMqttActionListener {
                    override fun onSuccess(token: IMqttToken?) {
                        client?.subscribe(MqttConfig.TOPIC_TV, MqttConfig.QOS)
                        Log.d(TAG, "✅ TV suscrita a ${MqttConfig.TOPIC_TV}")
                    }

                    override fun onFailure(token: IMqttToken?, ex: Throwable?) {
                        Log.e(TAG, "❌ Error de conexión: ${ex?.message}")
                    }
                })
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error al conectar: ${e.message}")
            }
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