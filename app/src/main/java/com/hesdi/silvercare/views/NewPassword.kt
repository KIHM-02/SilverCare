package com.hesdi.silvercare.views

import android.content.Intent
import android.os.Bundle
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
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.hesdi.silvercare.ui.theme.SilverCareTheme
import com.hesdi.silvercare.ui.theme.amarillo
import com.hesdi.silvercare.ui.theme.azulCielo
import com.hesdi.silvercare.ui.theme.azulRey
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.auth

class NewPassword : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
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
        var nuevaContrasena by remember { mutableStateOf("") }
        var confirmarContrasena by remember { mutableStateOf("") }
        var mensaje by remember { mutableStateOf("") }
        var colorMensaje by remember { mutableStateOf(Color.Red) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            azulRey, azulCielo
                        )
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
                    value = nuevaContrasena,
                    onValueChange = { nuevaContrasena = it },
                    label = {
                        Text(
                            text = "Nueva contraseña",
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = confirmarContrasena,
                    onValueChange = { confirmarContrasena = it },
                    label = {
                        Text(
                            text = "Confirma tu contraseña",
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )

                Button(
                    onClick = {
                        if (nuevaContrasena.isBlank() || confirmarContrasena.isBlank()) {
                            mensaje = "Por favor llena todos los campos"
                            colorMensaje = Color.Red
                        } else if (nuevaContrasena != confirmarContrasena) {
                            mensaje = "Las contraseñas no coinciden"
                            colorMensaje = Color.Red
                        } else {
                            val user = Firebase.auth.currentUser
                            if (user != null) {
                                user.updatePassword(nuevaContrasena).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        mensaje = "Contraseña actualizada correctamente"
                                        colorMensaje = Color(0xFF4CAF50)

                                        val db = FirebaseFirestore.getInstance()
                                        val userId = user.uid
                                        val userRef = db.collection("usuarios").document(userId)

                                        userRef.update("contrasena", nuevaContrasena)
                                            .addOnSuccessListener {
                                                val intent =
                                                    Intent(this@NewPassword, Home::class.java)
                                                startActivity(intent)
                                                finish()
                                            }
                                            .addOnFailureListener {
                                                mensaje =
                                                    "Contraseña actualizada, pero error al guardar en Firestore"
                                                colorMensaje = Color.Yellow
                                            }
                                    }
                                }
                            } else {
                                mensaje = "Usuario no autenticado"
                                colorMensaje = Color.Red
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(15.dp)
                        .border(2.dp, Color.Transparent, RoundedCornerShape(12.dp))
                        .width(300.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = amarillo,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Guardar")
                }
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

