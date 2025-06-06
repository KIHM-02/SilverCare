package com.hesdi.silvercare.views

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.hesdi.silvercare.R
import com.hesdi.silvercare.ui.theme.SilverCareTheme
import com.hesdi.silvercare.ui.theme.amarillo
import com.hesdi.silvercare.ui.theme.azulCielo
import com.hesdi.silvercare.ui.theme.azulRey

class Registro : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SilverCareTheme {
                    RegistroFrame()
                }
            }
        }
    }

@Composable
fun RegistroFrame() {
    SilverCareTheme {
        var correo by remember { mutableStateOf("")}
        var contrasena by remember { mutableStateOf("")}
        var passwordVisible by remember { mutableStateOf(false) }
        var mensaje by remember { mutableStateOf(false)}


        var isLoading by remember { mutableStateOf(false) }

        val context = LocalContext.current
        val activity =(LocalContext.current as? Activity)
        val errores = validarcontrasena((contrasena))
        val erroresCorreo = validarCorreo(correo)

        val enable= erroresCorreo.isEmpty() && errores.isEmpty()

        Box(modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        azulRey, azulCielo
                    )
                )
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Registrar usuario",
                    fontWeight = FontWeight.Bold,
                    fontSize =30.sp,
                    color= Color.White,

                )
                Spacer(modifier = Modifier.height(70.dp) )
                Text(
                    text ="Correo",
                    color= MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 25.sp
                )
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text(
                        text = "Ingresa tu correo",
                        color = Color.White,
                        fontSize = 20.sp
                    ) },
                    isError = erroresCorreo.isNotEmpty()
                    ,
                    textStyle = TextStyle(color = Color.White)
                )
                Spacer(modifier = Modifier.height(20.dp) )
                if (erroresCorreo.isNotEmpty()){
                    Column {
                        erroresCorreo.forEach { error ->
                            Text(text = "❌ $error", color = amarillo, fontSize = 16.sp)
                        }
                    }
                }

                Text(
                    text = "contrasena",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 25.sp

                )
                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = { Text("Ingresar contrasena", color = Color.White, fontSize = 20.sp) },
                    textStyle = TextStyle(color = Color.White),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password),
                    isError= errores.isNotEmpty(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(id = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                                contentDescription = if (passwordVisible) "Ocultar contrasena" else "Mostrar contrasena",
                                tint = Color.White
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(15.dp) )
                if (errores.isNotEmpty()) {
                    Column {
                        errores.forEach { error ->
                            Text(text = "❌ $error", color = amarillo, fontSize = 16.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp) )
                Row (
                ){
                    Text(
                        text = "Ya tienes una cuenta?",
                        fontSize = 20.sp,
                        color= MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(
                        text="Ingresa",
                        fontSize = 20.sp,
                        fontWeight= FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        color = amarillo,
                        modifier= Modifier
                            .clickable{
                               activity?.finish()
                            }

                    )
                }
                Spacer(modifier = Modifier.height(20.dp) )
                OutlinedButton(
                    onClick = {
                        verificarCorreoEnFirebase(correo) { existe ->
                            if (existe) {
                                Toast.makeText(context, "❌ Este correo ya está registrado.", Toast.LENGTH_SHORT).show()
                            }else{
                                Registrar (correo,contrasena){ success ->
                                    if (success){
                                        Toast.makeText(context, "Usuario registrado", Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(context, "❌ Este correo ya está registrado.", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }

                    }
                    },
                    enabled = enable,
                    modifier = Modifier
                        .padding(15.dp)
                        .border(2.dp, Color.Gray, RoundedCornerShape(12.dp))
                        .width(300.dp),

                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = amarillo,
                    ),
                    shape = RoundedCornerShape(12.dp),

                    ) {
                    Text(
                        text = "Registrar",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )

                }
            }
        }

    }

}

fun Registrar(correo: String, contrasena: String, onResult: (Boolean) -> Unit) {
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo, contrasena)
        .addOnCompleteListener { task ->
            onResult(task.isSuccessful) // Retorna true si es exitoso, false si falla
        }
}
fun validarcontrasena(contrasena: String): List<String> {
    val errores = mutableListOf<String>()

    if (contrasena.length < 8) errores.add("Debe tener al menos 8 caracteres.")
    if (!contrasena.any { it.isUpperCase() }) errores.add("Debe incluir al menos una letra mayúscula.")
    if(!contrasena.any(){it.isLowerCase()})errores.add("Debe incluir al menos una letra miniscula")
    if (!contrasena.any { it.isDigit() }) errores.add("Debe incluir al menos un número.")
    return errores
}

fun validarCorreo(correo: String): List<String> {
    val erroresCorreo= mutableListOf<String>()

    val correoValidado = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z_.-]{5,}\$")
    if(correo.isBlank()){
        erroresCorreo.add("El correo no puede estar vacio")
    }else if(!correoValidado.matches(correo)){
        erroresCorreo.add("Formato de correo no valido")
    }
    return erroresCorreo
}


fun verificarCorreoEnFirebase(correo: String, callback: (Boolean) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.fetchSignInMethodsForEmail(correo)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val signInMethods = task.result?.signInMethods
                callback(!signInMethods.isNullOrEmpty()) // Si hay métodos, el correo existe
            } else {
                callback(false) // Error en la consulta o correo no registrado
            }
        }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SilverCareTheme {
        RegistroFrame()
    }
}