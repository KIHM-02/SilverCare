package com.hesdi.silvercare.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
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
import com.hesdi.silvercare.R
import com.hesdi.silvercare.entities.Login
import com.hesdi.silvercare.ui.theme.SilverCareTheme
import com.hesdi.silvercare.ui.theme.amarillo
import com.hesdi.silvercare.ui.theme.azulCielo
import com.hesdi.silvercare.ui.theme.azulRey


val login = Login()

class LoginView : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginFrame(login)
        }
    }
    override fun onStart() {
        super.onStart()
        val user = login.getUserId()
        if (user != null) {
            // El usuario ya ha iniciado sesión, redirigir a Home
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }
    }
}

@Composable
fun LoginFrame(login: Login) {

    SilverCareTheme {
        var correo by remember { mutableStateOf("")}
        var contrasenia by remember { mutableStateOf("")}
        var passwordVisible by remember { mutableStateOf(false) }

        val context = LocalContext.current

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
                Image(
                    painter = painterResource(id= R.drawable.applogo4),
                    contentDescription = "Logo",
                    modifier=Modifier
                        .size(250.dp)
                )
                Spacer(modifier = Modifier.height(30.dp) )
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
                    textStyle = TextStyle(color = Color.White)
                )

                Spacer(modifier = Modifier.height(20.dp) )

                Text(
                    text = "Contraseña",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 25.sp
                )
                OutlinedTextField(
                    value = contrasenia,
                    onValueChange = { contrasenia = it },
                    label = { Text("Ingresar contraseña", color = Color.White, fontSize = 20.sp) },
                    textStyle = TextStyle(color = Color.White),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password
                    ),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(id = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                                contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                tint = Color.White
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row{
                    Text(
                        text="Se te olvido tu contraseña",
                        fontSize = 15.sp,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(
                        text = "Recuperar contraseña",
                        color = Color.White,
                        fontSize = 15.sp,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        )
                }
                Spacer(modifier = Modifier.height(20.dp) )
                Row{
                    Text(
                        text = "Aun no tienes cuenta?",
                        fontSize = 20.sp,
                        color= MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyMedium,

                    )

                    Spacer(modifier = Modifier.width(15.dp))
                    Text(
                        text="Registrate",
                        fontSize = 20.sp,
                        fontWeight= FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        color = amarillo,
                        modifier= Modifier
                            .clickable{
                                val intent = Intent(context, Registro::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                context.startActivity(intent)
                            }

                    )
                }
                Spacer(modifier = Modifier.height(20.dp) )
                OutlinedButton(
                    onClick = {
                        login.iniciarSesion(correo,contrasenia) { success ->
                        if(success){
                            val intentHome = Intent(context,Home::class.java)
                            context.startActivity(intentHome)
                        }else{
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
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
                    ),
                    shape = RoundedCornerShape(12.dp),

                    ) {
                    Text(
                        text = "Iniciar sesión",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    SilverCareTheme {
        LoginFrame(login)
    }

}