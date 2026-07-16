package mx.edu.utng.prgs.smarthealthmonitor2.wear.mqtt

object MqttConfig {
    // ⚠️ Reemplaza con los datos de TU cluster HiveMQ
    const val BROKER_URL = "ssl://9bb7d9ae0c674dcbb38620bae9020f92.s1.eu.hivemq.cloud:8883"
    const val USERNAME = "smarthealth"
    const val PASSWORD = "SmartHealth2025!"  // Reemplaza con tu contraseña real

    // Topics del proyecto
    const val TOPIC_FC = "utng/smarthealthmonitor/fc"
    const val TOPIC_TV = "utng/smarthealthmonitor/tv"
    const val TOPIC_ALERT = "utng/smarthealthmonitor/alerta"

    // QoS: 0=best effort, 1=at least once, 2=exactly once
    const val QOS = 1

    // Client IDs únicos por dispositivo
    const val CLIENT_WEAR = "smarthealthmonitor-wear"
    const val CLIENT_APP = "smarthealthmonitor-app"
    const val CLIENT_TV = "smarthealthmonitor-tv"
}