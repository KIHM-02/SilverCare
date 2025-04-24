package com.hesdi.silvercare.views

import android.content.ContentValues
import android.content.Context
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hesdi.silvercare.IniciarSesion
import com.hesdi.silvercare.Model.Citaviewmodel
import com.hesdi.silvercare.ui.theme.azulCielo
import com.hesdi.silvercare.ui.theme.azulRey
import com.hesdi.silvercare.ui.theme.SilverCareTheme
import com.hesdi.silvercare.components.*
import com.hesdi.silvercare.R
import com.hesdi.silvercare.entities.Cita
import com.hesdi.silvercare.ui.theme.amarillo
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import java.util.TimeZone



class AgendarCita : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_CALENDAR),
                100
            )
        }
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

    val viewModel = Citaviewmodel()

    var idUpdate by remember { mutableStateOf("") }
    var showDialog by remember {
        mutableStateOf(false)
    }
    var showHora by remember {
        mutableStateOf(false)
    }
    val state = rememberDatePickerState()
    var selectDay by remember { mutableStateOf("") }
    var medico by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    var lugar by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var context = LocalContext.current

    var validar = medico.isEmpty() && especialidad.isEmpty() && lugar.isEmpty()


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
            Spacer(modifier = Modifier.height(30.dp))
            OutlinedInputs("Nombre del Medico", medico,) { medico = it }
            SpaceTopBottom(15)
            OutlinedInputs("Ingresa la especialidad", especialidad) { especialidad = it }
            SpaceTopBottom(15)
            OutlinedInputs("Ingrese lugar", lugar) { lugar = it }
            SpaceTopBottom(15)
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextosSimples("Ingresa la fecha", Color.White)
                SpaceBetween(10)
                OutlinedButton(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = amarillo,
                        contentColor = Color.White
                    )
                ) {
                    Row() {
                        TextosSimples("fecha", Color.White,)
                        Spacer(modifier = Modifier.width(15.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                            contentDescription = "Imagen de calendario"

                        )
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextosSimples("Ingresa la hora", Color.White)
                SpaceBetween(10)
                OutlinedButton(
                    onClick = { showHora = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = amarillo,
                        contentColor = Color.White
                    )
                ) {
                    Row() {
                        TextosSimples("hora", Color.White,)
                        Spacer(modifier = Modifier.width(15.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.time),
                            contentDescription = "Imagen de calendario"

                        )
                    }
                }
            }
            SpaceTopBottom(15)
            if (showDialog) {
                DatePickerDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        Button(
                            onClick = {
                                state.selectedDateMillis?.let { millis ->
                                    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                    formatter.timeZone= TimeZone.getTimeZone("UTC")
                                    selectDay = formatter.format(Date(millis))
                                }
                                Log.d("------------------>>>> Feccha: ", selectDay)
                                showDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = amarillo,
                                contentColor = Color.White
                            )
                        ) {
                            TextosSimples("Confirmar", Color.White)
                        }
                    },
                    dismissButton = {
                        OutlinedButton(onClick = { showDialog = false }) {
                            TextosSimples("Cancelar", amarillo)
                        }
                    }

                ) {
                    DatePicker(state = state)
                }
            }
            if (showHora) {
                SelectorHora(hora) { hora = it }

            }


            Row {
                OutlinedButton(
                    onClick = {
                        val citas = Cita(
                            medico = medico,
                            especializacion = especialidad,
                            fecha = selectDay,
                            hora = hora,
                            Lugar = lugar,
                        )
                        viewModel.agregarCita(citas)
                        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        val fechaCompleta = formatter.parse("$selectDay $hora")
                        fechaCompleta?.let {
                            val timeInMillis = it.time
                            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            val fechaCompleta = formatter.parse("$selectDay $hora")
                            fechaCompleta?.let {
                                val timeInMillis = it.time
                                agregarEventoCalendario(context, "Cita médica", "Tu cita con $medico", timeInMillis)
                            }
                        }
                        Log.d("MEdico",medico)
                        Log.d("Especialidad",especialidad)
                        Log.d("hora",hora)
                        Log.d("lugar",lugar)

                        medico = ""
                        especialidad = ""
                        lugar = ""
                        showHora = false


                    },
                    modifier = Modifier
                        .padding(7.dp)
                        .border(2.dp, Color.Transparent, RoundedCornerShape(12.dp))
                        .width(125.dp),

                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = amarillo,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),

                    ) {
                    Text(
                        text = "Registrar",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

            }

        }

    }
}




fun agregarEventoCalendario(context: Context, titulo: String, descripcion: String, fecha: Long) {
    val calIntent = Intent(Intent.ACTION_INSERT).apply {
        type = "vnd.android.cursor.item/event"
        putExtra(CalendarContract.Events.TITLE, titulo)
        putExtra(CalendarContract.Events.DESCRIPTION, descripcion)
        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, fecha)
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, fecha + 60 * 60 * 1000) // 1 hora después
        putExtra(CalendarContract.Reminders.MINUTES, 1440) // 24 horas antes (en minutos)
        putExtra(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
    }
    context.startActivity(calIntent)
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    SilverCareTheme {
        CitaScreen()
    }
}
