package com.hesdi.silvercare.Views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hesdi.silvercare.IniciarSesion
import com.hesdi.silvercare.ui.theme.SilverCareTheme
import com.hesdi.silvercare.ui.theme.amarillo
import com.hesdi.silvercare.ui.theme.azulCielo
import com.hesdi.silvercare.ui.theme.azulRey

class Home : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SilverCareTheme {
                 HomeFrame()
                }
            }
        }
    }


@Composable
fun HomeFrame() {
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
        OutlinedButton(
            onClick = {
            },
            modifier = Modifier
                .padding(15.dp)
                .border(2.dp, Color.Transparent, RoundedCornerShape(12.dp))
                .width(500.dp),

            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = amarillo,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(12.dp),

            ) {
            Text(
                text = "Medicamentos",
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )

        }
        OutlinedButton(
            onClick = {},
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    SilverCareTheme {
        HomeFrame()
    }
}