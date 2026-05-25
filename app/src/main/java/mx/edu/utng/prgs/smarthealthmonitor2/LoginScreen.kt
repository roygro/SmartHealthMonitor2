package mx.edu.utng.prgs.smarthealthmonitor2

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mx.edu.utng.prgs.smarthealthmonitor2.ui.theme.SmartHealthMonitor2Theme
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit = {}) {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    fun validar(): Boolean {
        emailError = when {
            email.isBlank() -> "El email no puede estar vacío"
            !email.contains("@") -> "Email inválido (debe contener @)"
            else -> ""
        }
        passwordError = when {
            password.isBlank() -> "La contraseña no puede estar vacía"
            password.length < 6 -> "La contraseña debe tener mínimo 6 caracteres"
            else -> ""
        }
        return emailError.isEmpty() && passwordError.isEmpty()
    }

    SmartHealthMonitor2Theme {
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // Logo
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "SmartHealth Logo",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(80.dp)
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "SmartHealth Monitor 2",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.height(32.dp))

                // Campo email
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        if (emailError.isNotEmpty()) emailError = ""
                    },
                    label = { Text("Correo electrónico") },
                    isError = emailError.isNotEmpty(),
                    supportingText = {
                        if (emailError.isNotEmpty())
                            Text(emailError, color = MaterialTheme.colorScheme.error)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                // Campo contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        if (passwordError.isNotEmpty()) passwordError = ""
                    },
                    label = { Text("Contraseña") },
                    isError = passwordError.isNotEmpty(),
                    supportingText = {
                        if (passwordError.isNotEmpty())
                            Text(passwordError, color = MaterialTheme.colorScheme.error)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null)
                    },
                    visualTransformation = if (showPassword)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(
                            onClick = { showPassword = !showPassword }
                        ) {
                            Text(
                                if (showPassword) "OCULTAR" else "MOSTRAR",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(24.dp))

                // Botón ENTRAR
                Button(
                    onClick = {
                        if (validar()) {
                            isLoading = true
                            scope.launch {
                                delay(1500)
                                isLoading = false
                                onLoginSuccess()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("ENTRAR", style = MaterialTheme.typography.labelLarge)
                    }
                }

                Spacer(Modifier.height(16.dp))

                TextButton(onClick = {}) {
                    Text("¿Olvidaste tu contraseña?")
                }

            }
        }
    }
}

// Previews
@Preview(name = "Login - Light", showBackground = true)
@Preview(name = "Login - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Login - Big Font", showBackground = true, fontScale = 1.5f)
@Composable
private fun LoginScreenPreview() {
    SmartHealthMonitor2Theme {
        LoginScreen()
    }
}