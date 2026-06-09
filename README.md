# SmartHealth Monitor

![Android CI](https://github.com/roygro/SmartHealthMonitor2/actions/workflows/build.yml/badge.svg)
![Version](https://img.shields.io/badge/version-v1.0.0-blue)
![Android](https://img.shields.io/badge/Android-API26+-green)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-MD3-purple)

Aplicación Android multiplataforma para monitoreo de salud personal.

Desarrollada como proyecto integrador en UTNG — 9° Cuatrimestre 2025.

## Stack tecnológico

- Kotlin + Jetpack Compose
- Material Design 3
- Wearable Data Layer API (Wear OS)
- Health Services API (sensor FC real en segundo plano)
- Room Database (historial persistente offline)
- Jetpack Navigation + StateFlow
- Android TV / Leanback + Media3 (próximas unidades)

## Pantallas implementadas (Unidad I)

- [x] LoginScreen — S4
- [x] DashboardScreen — S5
- [x] HistorialScreen — S7 (Room + persistencia local con Flow)
- [x] AlertaScreen — S8 (AlertDialog MD3 + Snackbar de confirmación)

## Próximas unidades

- [ ] Android TV — S10-S12

## Capturas de pantalla

<div align="center">
  <table>
    <tr>
      <td align="center"><b>Login</b><br/><img src="screenshots/login.png" width="200"/></td>
      <td align="center"><b>Dashboard</b><br/><img src="screenshots/dashboard.png" width="200"/></td>
    </tr>
    <tr>
      <td align="center"><b>Historial</b><br/><img src="screenshots/historial.png" width="200"/></td>
      <td align="center"><b>Alerta (diálogo)</b><br/><img src="screenshots/alerta.png" width="200"/></td>
    </tr>
  </table>
</div>

### Snackbar de confirmación

<div align="center">
  <img src="screenshots/snackbar.png" width="200"/>
</div>

## Flujo completo de la app (v1.0.0)

1. **Login** → Autenticación básica con validación
2. **Dashboard** → Frecuencia cardíaca y pasos en tiempo real (simulado o desde Wear OS)
3. **Historial** → Lecturas guardadas en Room, persisten al cerrar la app
4. **Alerta** → Botón flotante rojo → Diálogo MD3 → Confirmación → Snackbar

## Autor

Princes Rocio Guerrero Sánchez — UTNG — princesgro@utng.edu.mx
