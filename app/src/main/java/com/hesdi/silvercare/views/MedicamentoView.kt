package com.hesdi.silvercare.views

import android.os.Bundle
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.hesdi.silvercare.components.Divisor
import com.hesdi.silvercare.components.ImagePicker
import com.hesdi.silvercare.components.OutlinedInputs
import com.hesdi.silvercare.components.OutlinedNumberInput
import com.hesdi.silvercare.components.SelectorHora
import com.hesdi.silvercare.components.SpaceBetween
import com.hesdi.silvercare.components.SpaceTopBottom
import com.hesdi.silvercare.components.TextosSimples
import com.hesdi.silvercare.components.Titulo
import com.hesdi.silvercare.entities.Login
import com.hesdi.silvercare.entities.Medicamento
import com.hesdi.silvercare.ui.theme.SilverCareTheme
import com.hesdi.silvercare.ui.theme.amarillo
import com.hesdi.silvercare.ui.theme.azulCielo
import com.hesdi.silvercare.ui.theme.azulRey

class MedicamentoView: ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SilverCareTheme {
                MedicamentoFrame()
            }
        }
    }
}


@Composable
fun MedicamentoFrame()
{
    val context = LocalContext.current
    var nombre by remember { mutableStateOf("") }
    var periodo by remember { mutableStateOf(TextFieldValue()) }
    var intervalo by remember { mutableStateOf(TextFieldValue()) }
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

            Titulo("Registrar Medicina")

            SpaceTopBottom(30)

            //OutlinedInputs("Medicamento","Registrar medicamento")
            OutlinedInputs("Nombre medicamento",nombre) {nombre = it}

            SpaceTopBottom(40)

            Row(verticalAlignment = Alignment.CenterVertically) {
                TextosSimples("Subir foto", Color.White)
                SpaceBetween(20)
                ImagePicker()
            }

            Divisor(30, 30, Color.White)

            Text(
                text = "Repetición del tratamiento",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )

            SpaceTopBottom(20)

            //OutlinedInputs("Periodo del tratamiento",periodo) {periodo = it}
            OutlinedNumberInput(
                "Periodo del tratamiento",
                number = periodo,
                onNumberChange = { periodo = it }
            )

            SpaceTopBottom(20)

            OutlinedNumberInput(
                "¿Cada cuanto tiempo?",
                number = intervalo,
                onNumberChange = { intervalo = it }
            )

            SpaceTopBottom(40)

            SelectorHora{hora = it }

            SpaceTopBottom(40)

            Button(
                onClick = {
                    callInsertData( context, nombre, periodo, intervalo, hora)
                    nombre = ""
                    periodo = TextFieldValue()
                    intervalo = TextFieldValue()
                    hora = ""
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

fun callInsertData(
    context: android.content.Context,
    nombre: String,
    periodo: TextFieldValue,
    intervalo: TextFieldValue,
    hora: String,
) {
    val userId = Login().getUserId()
    val medicamento = Medicamento(
        nombre,
        "",
        periodo.text.toInt(),
        intervalo.text.toInt(),
        hora, userId.toString()
    )

    if(!medicamento.verifyAlarmManager(context))
    {
        Toast.makeText(context, "Necesitas habilitar los permisos de alarma", Toast.LENGTH_LONG).show()
        return
    }
    else if(!medicamento.verifyNotification(context))
    {
        Toast.makeText(context, "Necesitas habilitar los permisos de notificación", Toast.LENGTH_LONG).show()
        return
    }

    val state = medicamento.insertData()

    if (state)
    {
        medicamento.scheduleNotification(context)
        Toast.makeText(context, "Medicamento registrado", Toast.LENGTH_SHORT).show()
    }
    else
        Toast.makeText(context, "Error al registrar el medicamento", Toast.LENGTH_SHORT).show()
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
        MedicamentoFrame()
    }
}
