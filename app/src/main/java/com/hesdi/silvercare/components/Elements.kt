package com.hesdi.silvercare.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.hesdi.silvercare.R
import com.hesdi.silvercare.ui.theme.amarillo
import com.hesdi.silvercare.ui.theme.azulRey
import java.util.Calendar

/* Texto usado para titulos */
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
fun TextosPequenios(text: String, color: Color)
{
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
}

@Composable
fun Titulo(text: String) {
    Text(
        text = text,
        fontSize = 50.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        textAlign = TextAlign.Center,
        fontFamily = FontFamily.Cursive
    )
}

@Composable
fun Divisor(topSpace: Int, bottomSpace: Int, color: Color) {
    Column (
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(horizontal = 35.dp)
    ){
        SpaceTopBottom(topSpace)
        HorizontalDivider(thickness = 2.dp, color = color)
        SpaceTopBottom(bottomSpace)
    }
}

/*
* Lee una entrada de texto y la muestra en la pantalla, permitiendo almacenar el valor en el metodo
* donde se este llamando.
*
* Para ello ocupas crear una variable tipo remember { mutableStateOf(tipo_dato) }
* el cual le pasaras como parametro al metodo OutlinedInputs
*
* ejemplo:
*
* var nombre by remember { mutableStateOf("") }
*
* OutlinedInputs("Nombre",nombre) {nombre = it}
* */
@Composable
fun OutlinedInputs(title: String, text: String, onValueChange: (String) -> Unit)
{
    OutlinedTextField(
        value = text,
        onValueChange = { onValueChange(it.take(30)) } ,
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
            unfocusedIndicatorColor = Color.White,
            cursorColor = Color.White
        )
    )
}

/*
* lee una cadena de texto y la muestra en la pantalla, permitiendo almacenar el valor en el metodo
* Desde fuera se controla este metodo, es decir, no envias el valor mutable, sino que lo manejas desde
* fuera
 */
@Composable
fun OutlinedNumberInput(title: String, number: TextFieldValue, onNumberChange: (TextFieldValue) -> Unit)
{
    OutlinedTextField(
        value = number,
        onValueChange = {
            if (it.text.all(Char::isDigit) and (it.text.length <= 3)){
                onNumberChange(it)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
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
            unfocusedIndicatorColor = Color.White,
            cursorColor = Color.White
        )
    )
}

@Composable
fun SpaceTopBottom(dimension: Int) {
    Spacer(modifier = Modifier.height(dimension.dp))
}

/*Son los espacios que hay entre elementos*/
@Composable
fun SpaceBetween(dimension: Int) {
    Spacer(modifier = Modifier.width(dimension.dp))
}

/* Permite recuperar una imagen almacenada en la galeria del usuario para poder mostrarla, esta
* funcion no requiere de permisos para funcionar
*/
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

/* Permite seleccionar una hora y mostrarla en la pantalla */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorHora(
    onHoraSeleccionada: (String) -> Unit
) {
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    val horaFormateada = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)

    // Actualiza el valor en cada recomposición (opcional)
    LaunchedEffect(timePickerState.hour, timePickerState.minute) {
        onHoraSeleccionada(horaFormateada)
    }

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
                text = "Seleccionar hora/ 24 hrs",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            SpaceTopBottom(10)

            TimeInput(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    clockDialColor = Color.Green,
                    clockDialSelectedContentColor = Color.Black,
                    clockDialUnselectedContentColor = Color.Yellow,
                    periodSelectorSelectedContainerColor = Color.Green,
                    periodSelectorUnselectedContainerColor = Color.Yellow
                )
            )

            SpaceTopBottom(10)

            Text(
                text = "Hora seleccionada: $horaFormateada",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }

    @Composable
    fun selector()
    {
        var formatoTiempo by remember { mutableStateOf("") }
        var expanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 35.dp, vertical = 16.dp)
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = amarillo)
                    .clickable { expanded = !expanded }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                TextosPequenios("Seleccionar fecha", Color.Black)

                SpaceBetween(8)

                Icon(
                    painter = painterResource(R.drawable.calendar_icon),
                    tint = Color.Black,
                    contentDescription = "Selector de fecha")

                SpaceBetween(8)

                TextosPequenios(formatoTiempo, azulRey)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {expanded = false}
            ) {
                DropdownMenuItem(
                    text = { Text("Dias")},
                    onClick = { formatoTiempo = "Dias" }
                )
                DropdownMenuItem(
                    text = { Text("Semanas")},
                    onClick = { formatoTiempo = "Semanas" }
                )
                DropdownMenuItem(
                    text = { Text("Meses")},
                    onClick = { formatoTiempo = "Meses " }
                )
            }
        }

    }
}