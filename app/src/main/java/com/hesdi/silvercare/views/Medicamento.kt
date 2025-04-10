package com.hesdi.silvercare.views

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.hesdi.silvercare.components.ImagePicker
import com.hesdi.silvercare.components.OutlinedInputs
import com.hesdi.silvercare.components.selectorHora
import com.hesdi.silvercare.components.SpaceBetween
import com.hesdi.silvercare.components.SpaceTopBottom
import com.hesdi.silvercare.components.TextosSimples
import com.hesdi.silvercare.ui.theme.SilverCareTheme
import com.hesdi.silvercare.ui.theme.amarillo
import com.hesdi.silvercare.ui.theme.azulCielo
import com.hesdi.silvercare.ui.theme.azulRey

class Medicamento: ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SilverCareTheme {
                MedicamentoView()
            }
        }
    }
}


@Composable
fun MedicamentoView()
{
    val context = LocalContext.current
    var medicamento by remember { mutableStateOf("") }
    var caducidad by remember { mutableStateOf("") }
    var hora by remember {mutableStateOf("")}

    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    azulRey,
                    azulCielo
                )
            )
        )
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()), //Permite hacerlo scrollable
            horizontalAlignment = Alignment.CenterHorizontally)
        {

            SpaceTopBottom(100)

            Text(
                text = "Registro de Medicina",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            SpaceTopBottom(40)

            //OutlinedInputs("Medicamento","Registrar medicamento")
            OutlinedInputs("Medicamento",medicamento) {medicamento = it}

            SpaceTopBottom(40)

            Row(verticalAlignment = Alignment.CenterVertically) {
                TextosSimples("Subir foto", Color.White)
                SpaceBetween(20)
                ImagePicker()
            }

            SpaceTopBottom(20)

            OutlinedInputs("Fecha de caducidad",caducidad) {caducidad = it}

            SpaceTopBottom(40)

            hora = selectorHora()

            SpaceTopBottom(40)

            Button(
                onClick = {
                    Toast.makeText(context, medicamento, Toast.LENGTH_SHORT).show()
                    Log.d("------------------>>>> Medicamento: ",medicamento)
                    Log.d("------------------>>>> Caducidad: ",caducidad)
                    Log.d("------------------>>>> Hora: ",hora)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = amarillo,
                    contentColor = Color.Black
                )) {
                TextosSimples("Registrar", Color.White)
            }

            SpaceTopBottom(50)
        }
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true,
    showSystemUi = true)
@Preview(
    showBackground = true,
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true
)
@Composable
fun PruebaPreview()
{
    SilverCareTheme {
        MedicamentoView()
    }
}
