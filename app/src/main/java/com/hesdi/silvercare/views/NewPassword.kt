package com.hesdi.silvercare.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.hesdi.silvercare.ui.theme.SilverCareTheme
import com.hesdi.silvercare.ui.theme.amarillo
import com.hesdi.silvercare.ui.theme.azulCielo
import com.hesdi.silvercare.ui.theme.azulRey

class NewPassword : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SilverCareTheme {
                NewPasswordScreen()
            }
        }
    }

    @Composable
    fun NewPasswordScreen() {
        var contrasenaActual by remember { mutableStateOf("") }
        var nuevaContrasena by remember { mutableStateOf("") }
        var confirmarContrasena by remember { mutableStateOf("") }
        var mensaje by remember { mutableStateOf("") }
        var colorMensaje by remember { mutableStateOf(Color.Red) }
        var isLoading by remember { mutableStateOf(false) }

        //Debe de tener minimo 6 caracteres
        fun isPasswordStrong(password: String): Pair<Boolean, String> {
            if (password.length < 6) {
                return false to "La contraseña debe tener al menos 6 caracteres"
            }
            return true to ""
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(azulRey, azulCielo)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Cambiar contraseña", color = Color.White, fontSize = 24.sp)

                OutlinedTextField(
                    value = contrasenaActual,
                    onValueChange = { contrasenaActual = it },
                    label = {
                        Text("Contraseña actual", color = Color.White, fontSize = 20.sp)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    enabled = !isLoading
                )

                OutlinedTextField(
                    value = nuevaContrasena,
                    onValueChange = { nuevaContrasena = it },
                    label = {
                        Text("Nueva contraseña", color = Color.White, fontSize = 20.sp)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    enabled = !isLoading
                )

                OutlinedTextField(
                    value = confirmarContrasena,
                    onValueChange = { confirmarContrasena = it },
                    label = {
                        Text("Confirma tu contraseña", color = Color.White, fontSize = 20.sp)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    enabled = !isLoading
                )

                Button(
                    onClick = {
                        val user = Firebase.auth.currentUser
                        val email = user?.email

                        if (contrasenaActual.isBlank() || nuevaContrasena.isBlank() || confirmarContrasena.isBlank()) {
                            mensaje = "Por favor llena todos los campos"
                            colorMensaje = Color.Red
                            return@Button
                        }

                        if (nuevaContrasena != confirmarContrasena) {
                            mensaje = "Las contraseñas no coinciden"
                            colorMensaje = Color.Red
                            return@Button
                        }

                        val (isStrong, passwordError) = isPasswordStrong(nuevaContrasena)
                        if (!isStrong) {
                            mensaje = passwordError
                            colorMensaje = Color.Red
                            return@Button
                        }

                        if (contrasenaActual == nuevaContrasena) {
                            mensaje = "La nueva contraseña debe ser diferente a la actual"
                            colorMensaje = Color.Red
                            return@Button
                        }

                        if (user != null && email != null) {
                            isLoading = true
                            mensaje = "Cambiando contraseña..."
                            colorMensaje = Color.White

                            val credential = EmailAuthProvider.getCredential(email, contrasenaActual)

                            user.reauthenticate(credential).addOnCompleteListener { authTask ->
                                if (authTask.isSuccessful) {
                                    user.updatePassword(nuevaContrasena)
                                        .addOnCompleteListener { updateTask ->
                                            isLoading = false
                                            if (updateTask.isSuccessful) {
                                                Log.d("NewPassword", "Contraseña actualizada exitosamente")

                                                val db = FirebaseFirestore.getInstance()
                                                db.collection("usuarios").document(user.uid)
                                                    .update("ultimaActualizacionPassword", System.currentTimeMillis())
                                                    .addOnSuccessListener {
                                                        val intent = Intent(this@NewPassword, Home::class.java)
                                                        intent.putExtra("mensajeCambio", "Contraseña cambiada exitosamente")
                                                        startActivity(intent)
                                                        finish()
                                                    }
                                                    .addOnFailureListener { e ->
                                                        Log.w("NewPassword", "Error al actualizar timestamp", e)
                                                        // Aún así redirigir porque la contraseña sí se cambió
                                                        val intent = Intent(this@NewPassword, Home::class.java)
                                                        intent.putExtra("mensajeCambio", "Contraseña cambiada exitosamente")
                                                        startActivity(intent)
                                                        finish()
                                                    }
                                            } else {
                                                val error = updateTask.exception?.message ?: "Error desconocido"
                                                mensaje = "Error al cambiar la contraseña: $error"
                                                colorMensaje = Color.Red
                                                Log.e("NewPassword", "Error al actualizar contraseña", updateTask.exception)
                                            }
                                        }
                                } else {
                                    isLoading = false
                                    val error = authTask.exception?.message ?: "Contraseña incorrecta"
                                    mensaje = "Contraseña actual incorrecta: $error"
                                    colorMensaje = Color.Red
                                    Log.e("NewPassword", "Error en reautenticación", authTask.exception)
                                }
                            }
                        } else {
                            mensaje = "Usuario no autenticado"
                            colorMensaje = Color.Red
                        }
                    },
                    modifier = Modifier
                        .padding(15.dp)
                        .border(2.dp, Color.Transparent, RoundedCornerShape(12.dp))
                        .width(300.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = amarillo,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Guardar")
                    }
                }

                Text(text = mensaje, color = colorMensaje)
            }
        }
    }

    @Preview
    @Composable
    fun NewPasswordPreview() {
        SilverCareTheme {
            NewPasswordScreen()
        }
    }
}