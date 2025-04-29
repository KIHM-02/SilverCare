package com.hesdi.silvercare.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hesdi.silvercare.Model.Citaviewmodel
import com.hesdi.silvercare.R
import com.hesdi.silvercare.components.SpaceTopBottom
import com.hesdi.silvercare.components.Titulo
import com.hesdi.silvercare.ui.theme.SilverCareTheme
import com.hesdi.silvercare.ui.theme.amarillo

class ListCitas : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SilverCareTheme {
                ListCitasView()
            }
        }
    }
}


@Composable
fun ListCitasView() {
    val viewModel = Citaviewmodel()
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val listaCitas by viewModel.listaCitas.collectAsState()
    val context = LocalContext.current
    val activity =(LocalContext.current as? Activity)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C2D4A))
            .padding(16.dp)
    ) {
        Titulo("    Lista de citas")
        // Botón superior
        Row (
            modifier = Modifier
                .fillMaxWidth()
        ){
            OutlinedButton(
                onClick = {
                    val intent =  Intent(context, AgendarCita::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .padding(10.dp)
                    .border(2.dp, Color.Transparent, RoundedCornerShape(12.dp))
                    .width(175.dp),

                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = amarillo,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp),

                ) {
                Text(
                    text = "Agregar Cita",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            OutlinedButton(
                onClick = {
                    viewModel.obtenerCitas()
                },
                modifier = Modifier
                    .padding(10.dp)
                    .border(2.dp, Color.Transparent, RoundedCornerShape(12.dp))
                    .width(175.dp),

                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = amarillo,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp),

                ) {
                Text(
                    text = "Recargar lista",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

        }


        // Barra de búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = null)
            },
            placeholder = { Text("Buscar Cita medica") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White
            )
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .size(300.dp)
        ) {
            items(listaCitas) { Cita ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFA3C525), shape = RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = Cita.especializacion, fontSize = 20.sp, color = Color.Black)
                            Text(
                                text = Cita.fecha,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            Text(text = Cita.hora,
                                fontSize = 16.sp,
                                color = Color.Black

                            )
                        }
                        // Debo reemplazar icono por imagen
                        Image(
                            painter = painterResource(id = R.drawable.ic_visibility_off),
                            contentDescription = "Icono medicamento",
                            modifier = Modifier
                                .size(40.dp)
                                .padding(start = 8.dp)
                        )
                    }
                }
                SpaceTopBottom(10)
            }
        }
        // Botón de regreso
        IconButton(
            onClick = {
                activity?.finish()
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 12.dp)
        ) {
            Icon(
                //TODO: Reemplazar por imagen
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = Color(0xFFA3C525),
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

@Preview
@Composable
fun ListPreviewCitas() {
    SilverCareTheme {
        ListCitasView()
    }
}