package com.hesdi.silvercare.views

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hesdi.silvercare.ui.theme.azulCielo
import com.hesdi.silvercare.ui.theme.azulRey
import com.hesdi.silvercare.ui.theme.SilverCareTheme
import com.hesdi.silvercare.components.*
import com.hesdi.silvercare.R
import com.hesdi.silvercare.ui.theme.amarillo
import java.time.Instant
import java.time.ZoneId


class AgendarCita : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SilverCareTheme {
                CitaScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitaScreen() {
    var showDialog by remember {
        mutableStateOf(false)
    }
    val state = rememberDatePickerState()
    var medico by remember { mutableStateOf("")}
    var especialidad by remember { mutableStateOf("")}
    Box(
        modifier = Modifier
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
            Titulo("Agendar cita")
            Spacer(modifier = Modifier.height(70.dp))
            OutlinedInputs("Medico", medico) {medico = it }
            SpaceTopBottom(30)
            OutlinedInputs("Ingresa la especialidad",especialidad) { especialidad=it}
            SpaceTopBottom(30)
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextosSimples("Ingresa la fecha", Color.White)
                SpaceBetween(25)
                OutlinedButton(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = amarillo,
                        contentColor = Color.White
                    )
                ) {
                    Row() {
                        TextosSimples("fecha", Color.White)
                        Spacer(modifier = Modifier.width(15.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                            contentDescription = "Imagen de calendario"

                        )
                    }
                }
            }
            if (showDialog) {
                DatePickerDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        Button(
                            onClick = { showDialog = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = amarillo,
                                contentColor = Color.White
                            )
                        ) {
                            TextosSimples("Confirmar", Color.White)
                        }
                    },
                    dismissButton = {
                        OutlinedButton(onClick = {showDialog=false}) {
                            TextosSimples("Cancelar", amarillo)
                        }
                    }

                ) {
                    DatePicker(state = state)
                }
            }
            var date= state.selectedDateMillis
            date?.let {
                val LocalDay = Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
                TextosSimples("Fecha seleccionada ${LocalDay.dayOfMonth}/${LocalDay.monthValue}/${LocalDay.year}",Color.White )
            }

        }
    }

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    SilverCareTheme {
        CitaScreen()
    }
}