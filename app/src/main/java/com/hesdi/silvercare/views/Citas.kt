package com.hesdi.silvercare.views

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.hesdi.silvercare.LoginFrame
import com.hesdi.silvercare.R
import com.hesdi.silvercare.ui.theme.SilverCareTheme
import com.hesdi.silvercare.ui.theme.azulCielo
import com.hesdi.silvercare.ui.theme.azulRey
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class Appointment(
    val doctor: String,
    val date: LocalDate,
    val address: String,
    val clinicNumber: String
)

class Citas : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SilverCareTheme {
                CitasFrame()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitasFrame() {
    val context = LocalContext.current

    // Flags para mostrar cada diálogo
    var showNameDialog     by remember { mutableStateOf(false) }
    var showDateDialog     by remember { mutableStateOf(false) }
    var showAddressDialog  by remember { mutableStateOf(false) }

    // Variables temporales para capturar datos
    var tempDoctor   by remember { mutableStateOf("") }
    var tempDate     by remember { mutableStateOf(LocalDate.now()) }
    var tempAddress  by remember { mutableStateOf("") }
    var tempClinic   by remember { mutableStateOf("") }

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
    ){
        Button(
            onClick = { showNameDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB0E000)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Programar cita", color = Color.Black)
        }

        // Diálogo de NOMBRE
        if (showNameDialog) {
            Dialog(onDismissRequest = { showNameDialog = false }) {
                Card(shape = RoundedCornerShape(16.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Ingresa nombre del médico", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = tempDoctor,
                            onValueChange = { tempDoctor = it },
                            placeholder = { Text("Nombre del médico") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(12.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(onClick = { showNameDialog = false }) {
                                Text("Cancelar")
                            }
                            Spacer(Modifier.width(8.dp))
                            Button(onClick = {
                                showNameDialog = false
                                showDateDialog = true
                            }) {
                                Text("Continuar")
                            }
                        }
                    }
                }
            }
        }

        // Diálogo de FECHA
        if (showDateDialog) {
            Dialog(onDismissRequest = { showDateDialog = false }) {
                Card(shape = RoundedCornerShape(16.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Selecciona una fecha", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        // DatePicker de Material3
                        val datePickerState = rememberDatePickerState(
                            initialSelectedDateMillis = tempDate
                                .atStartOfDay(ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli()
                        )
                        DatePicker(state = datePickerState)
                        Spacer(Modifier.height(12.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            TextButton(onClick = {
                                showDateDialog = false
                                showNameDialog = true
                            }) {
                                Text("Regresar")
                            }
                            Button(onClick = {
                                // actualizar tempDate desde millis
                                datePickerState.selectedDateMillis?.let { millis ->
                                    tempDate = Instant
                                        .ofEpochMilli(millis)
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                                }
                                showDateDialog = false
                                showAddressDialog = true
                            }) {
                                Text("Continuar")
                            }
                        }
                    }
                }
            }
        }

        // Diálogo de DIRECCIÓN + CLÍNICA
        if (showAddressDialog) {
            Dialog(onDismissRequest = { showAddressDialog = false }) {
                Card(shape = RoundedCornerShape(16.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Ingresa la dirección", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = tempAddress,
                            onValueChange = { tempAddress = it },
                            placeholder = { Text("Dirección") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = tempClinic,
                            onValueChange = { tempClinic = it },
                            placeholder = { Text("Número de clínica") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(12.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(onClick = {
                                showAddressDialog = false
                                showDateDialog = true
                            }) {
                                Text("Cancelar")
                            }
                            Spacer(Modifier.width(8.dp))
                            Button(onClick = {
                                showAddressDialog = false
                                // Este Toast es de prueba
                                Toast.makeText(
                                    context,
                                    "Médico: $tempDoctor\n" +
                                            "Fecha: $tempDate\n" +
                                            "Dirección: $tempAddress\n" +
                                            "Clínica: $tempClinic",
                                    Toast.LENGTH_LONG
                                ).show()
                                // reset campos
                                tempDoctor  = ""
                                tempDate    = LocalDate.now()
                                tempAddress = ""
                                tempClinic  = ""
                            }) {
                                Text("Finalizar")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppointmentCard(
    appointment: Appointment,
    onDelete: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB0E000)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            // ─── CABECERA ─────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFB0E000))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Próxima cita ${appointment.date.format(formatter)}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_visibility_off),
                        contentDescription = "Eliminar cita",
                        tint = Color.Black
                    )
                }
            }

            // ─── ESPACIO PARA IMAGEN ──────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.applogo4),
                    contentDescription = "Imagen del médico",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                )
            }

            // ─── DATOS INFERIORES ─────────────────────────────────
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Médico: ${appointment.doctor}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Dirección: ${appointment.address}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Clínica: ${appointment.clinicNumber}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CitasPreview() {
    SilverCareTheme {
        CitasFrame()
    }

}