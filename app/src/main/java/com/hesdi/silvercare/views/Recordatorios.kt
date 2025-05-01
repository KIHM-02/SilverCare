package com.hesdi.silvercare.views

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hesdi.silvercare.R
import com.hesdi.silvercare.ui.theme.SilverCareTheme

class Recordatorios : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SilverCareTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MedicamentosScreen()
                }
            }
        }
    }
}

@Composable
fun MedicamentosScreen() {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val medicamentos = List(6) { "Aquí va el medicamento" }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C2D4A))
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(50.dp))
        // Botón superior
        Button(
            onClick = {
                val intent = Intent(context, MedicamentoView::class.java)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA3C525)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Text("+ Agregar medicamento", color = Color.Black)
        }

        // Barra de búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = null)
            },
            placeholder = { Text("Buscar medicamentos") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White
            )
        )

        // Lista de medicamentos
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(medicamentos) { nombre ->
                MedicamentoCard(nombre)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Botón de regreso
        IconButton(
            onClick = { /* Regresar:)*/ },
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

@Composable
fun MedicamentoCard(nombre: String) {
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
                Text(text = nombre, fontSize = 16.sp, color = Color.Black)
                Text(text = "Aquí el nombre 100%flico", fontSize = 12.sp, color = Color.Black)
            }

            // Debo reemplazar icono por imagen
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Icono medicamento",
                modifier = Modifier
                    .size(40.dp)
                    .padding(start = 8.dp)
            )
        }
    }
}


@Preview
@Composable
fun MedicamentosScreenPreview() {
    SilverCareTheme {
        MedicamentosScreen()
    }
}