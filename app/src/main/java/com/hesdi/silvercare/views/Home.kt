package com.hesdi.silvercare.views

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hesdi.silvercare.R
import com.hesdi.silvercare.entities.Canal_Notificacion
import com.hesdi.silvercare.ui.theme.SilverCareTheme
import com.hesdi.silvercare.ui.theme.amarillo
import com.hesdi.silvercare.ui.theme.azulCielo
import com.hesdi.silvercare.ui.theme.azulRey

class Home : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val notificationChannel = Canal_Notificacion()
        notificationChannel.createChannel(this)

        enableEdgeToEdge()
        setContent {
            SilverCareTheme {
                HomeFrame(
                    onNavigatetoLogin = {
                        val intent = Intent(this, LoginView::class.java)
                        startActivity(intent)
                    },
                    onNavigatetoRecordatorios = {
                        val intent = Intent(this, Recordatorios::class.java)
                        startActivity(intent)
                    },
                    onNavigatetoCitas = {
                        val intent = Intent(this, ListCitas::class.java)
                        startActivity(intent)
                    },
                    onNavigatetoSOS = {
                        //val intent = Intent(this, SOS::class.java)
                        //startActivity(intent)
                    },
                    onNavigateToCambiarContrasena = {
                        val intent = Intent(this, NewPassword::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun HomeFrame(
    onNavigatetoLogin: () -> Unit = {},
    onNavigatetoRecordatorios: () -> Unit = {},
    onNavigatetoCitas: () -> Unit = {},
    onNavigatetoSOS: () -> Unit = {},
    onNavigateToCambiarContrasena: () -> Unit = {}
) {
    Box(modifier = Modifier
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(16.dp)
        ){
            SeccionPerfil(
                onNavigateToLogin = onNavigatetoLogin,
                onNavigateToCambiarContrasena = onNavigateToCambiarContrasena
            )

            MenuBotones(
                text = "Recordatorios",
                iconRes = R.drawable.baseline_access_time_24,
                onClick = onNavigatetoRecordatorios
            )
            MenuBotones(
                text = "Citas médicas",
                iconRes = R.drawable.baseline_calendar_month_24,
                onClick = onNavigatetoCitas
            )
            MenuBotones(
                text = "SOS",
                iconRes = R.drawable.baseline_sos_24,
                onClick = onNavigatetoSOS
            )
        }
    }
}

//Parte superior para visualizar el perfil
@Composable
fun SeccionPerfil(
    onNavigateToLogin: () -> Unit,
    onNavigateToCambiarContrasena: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(modifier = Modifier.weight(1f))

        // IconButton con DropdownMenu
        Box {
            IconButton(onClick = {expanded = true}) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Icono de Perfil",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    tint = Color(0xFF0F4C81)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Cambiar contraseña") },
                    onClick = onNavigateToCambiarContrasena
                )
                DropdownMenuItem(
                    text = { Text("Cerrar sesión") },
                    onClick = onNavigateToLogin
                )
            }
        }
    }
}

//Función de botones
@Composable
fun MenuBotones(
    text: String, iconRes: Int, onClick: () -> Unit
){
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = amarillo,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .height(50.dp)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = text,
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    SilverCareTheme {
        HomeFrame()
    }
}