package com.hesdi.silvercare.views


import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.hesdi.silvercare.R
import com.hesdi.silvercare.ui.theme.SilverCareTheme
import com.hesdi.silvercare.ui.theme.amarillo
import com.hesdi.silvercare.ui.theme.azulCielo
import com.hesdi.silvercare.ui.theme.azulRey
import java.util.Calendar

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
    var medicamento by remember { mutableStateOf("") }

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
            medicamento = OutlinedInputsTest("Medicamento","Registrar medicamento")

            SpaceTopBottom(40)

            Row(verticalAlignment = Alignment.CenterVertically) {
                TextosSimples("Subir foto", Color.White)
                SpaceBetween(20)
                ImagePicker()
            }

            SpaceTopBottom(20)

            OutlinedInputs("Fecha de caducidad","Registrar fecha de caducidad")

            SpaceTopBottom(40)

            SelectorHora()

            SpaceTopBottom(40)

            Button(
                onClick = {

                    ///TODO Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = amarillo,
                    contentColor = Color.Black
                ))
            {
                TextosSimples("Registrar", Color.White)
            }
        }
    }
}

@Composable
fun ImagePicker()
{
    /*
    * Fuente:
    * https://proandroiddev.com/implementing-photo-picker-on-android-kotlin-jetpack-compose-326e33e83b85
    * */

    //Almacenamos la foto obtenida por el usuario en una variable
    var photoUri: Uri? by remember { mutableStateOf(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia())
    { uri ->
        //Cuando el usuario selecciona una imagen, se ejecuta esta función
        photoUri = uri
    }

    Column(verticalArrangement = Arrangement.Center) {
        Button(
            onClick = {
                //Cuando presionen el boton, se ejecuta la funcion picker
                launcher.launch(
                    PickVisualMediaRequest(
                        //Aqui solo solicitamos fotos, pueden ser tambien videos pero ocupas
                        //cambiar a VideoOnly o ImageAndVideo
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = amarillo,
                contentColor = Color.Black
            )
        ) {
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_image_24),
                    contentDescription = "Ícono de imagen"
                )

                SpaceBetween(20)

                Text("Imagen")
            }
        }

        SpaceTopBottom(5)

        if (photoUri != null) {
            //Usamos Coil para mostrar la foto
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photoUri)
                    .build()
            )

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
            )

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorHora() {
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    // Caja blanca con esquinas redondeadas
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(Color.White, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Seleccionar hora",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            SpaceTopBottom(10)

            TimeInput(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    clockDialColor = Color.Green, // fondo del reloj
                    clockDialSelectedContentColor = Color.Black, // número seleccionado
                    clockDialUnselectedContentColor = Color.Yellow, // números no seleccionados
                    periodSelectorSelectedContainerColor = Color.Green, // AM/PM fondo seleccionado
                    periodSelectorUnselectedContainerColor = Color.Yellow // AM/PM fondo no seleccionado
                ))

            SpaceTopBottom(10)

            val horaFormateada = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)

            Text(
                text = "Hora seleccionada: $horaFormateada",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


@Composable
fun TextosSimples(text: String, color: Color)
{
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )
}

@Composable
fun SpaceBetween(dimension: Int)
{
    Spacer(modifier = Modifier.width(dimension.dp))
}

@Composable
fun SpaceTopBottom(dimension: Int)
{
    Spacer(modifier = Modifier.height(dimension.dp))
}

@Composable
fun OutlinedInputs(title: String, text: String)
{
    var string by remember { mutableStateOf(text) }

    OutlinedTextField(
        value = string,
        onValueChange = { string = it },
        label = {
            TextosSimples(text = title, amarillo)
        },
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = amarillo,
            cursorColor = Color.Yellow
        )
    )
}

@Composable
fun OutlinedInputsTest(title: String, text: String): String
{
    var string by remember { mutableStateOf(text) }

    OutlinedTextField(
        value = string,
        onValueChange = { string = it },
        label = {
            TextosSimples(text = title, amarillo)
        },
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = amarillo,
            cursorColor = Color.Yellow
        )
    )

    return string
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
