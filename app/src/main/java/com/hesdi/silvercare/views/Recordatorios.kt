package com.hesdi.silvercare.views

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.hesdi.silvercare.entities.Login
import com.hesdi.silvercare.entities.Medicamento
import com.hesdi.silvercare.ui.theme.SilverCareTheme

class Recordatorios : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        val medicamento = Medicamento()
//        var medicamentos = medicamento.searchData(login.getUserId().toString())
//
//        Toast.makeText(this, "Ya me ejecuteeeee", Toast.LENGTH_SHORT).show()
//        Log.d("CUENTA", "----------->Ya me ejecuteeeee ${login.getUserId().toString()}")
//
//        if(medicamentos.isEmpty())
//        {
//            Log.d("VALORES", "-----------> No hay medicamentos")
//        }
//        else {
//            for (med in medicamentos) {
//                Log.d("VALORES", "-----------> ${med.getNombre()}")
//            }
//        }

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
fun MedicamentosScreen()
{
    val login = Login()
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var medicamentos by remember { mutableStateOf<List<Medicamento>>(emptyList()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val medicamento = Medicamento()
        medicamentos = medicamento.searchData(login.getUserId().toString())
    }

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
        LazyColumn(modifier = Modifier.weight(1f))
        {
            items(medicamentos.filter {
                it.getNombre().contains(searchQuery.text, ignoreCase = true)
            }) { medicamento ->
                MedicamentoCard(medicamento)
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
fun MedicamentoCard(medicamento: Medicamento) {
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
                Text(text = "Medicamento: ${medicamento.getNombre()}", fontSize = 16.sp, color = Color.Black)
                Text(text = "Hora programada: ${medicamento.getHora()}", fontSize = 12.sp, color = Color.Black)
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