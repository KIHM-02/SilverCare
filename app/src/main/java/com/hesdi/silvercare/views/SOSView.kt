package com.hesdi.silvercare.views

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.hesdi.silvercare.ui.theme.SilverCareTheme
import com.hesdi.silvercare.ui.theme.amarillo
import com.hesdi.silvercare.ui.theme.azulCielo
import com.hesdi.silvercare.ui.theme.azulRey

class SOSView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SilverCareTheme {
                SOSframe()
            }
        }
    }
}

@Composable
fun SOSframe() {
    val context = LocalContext.current
    var savedPhoneNumber by remember { mutableStateOf("") }
    var savedContactName by remember { mutableStateOf("") }

    // Launcher para seleccionar contacto
    val contactPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { contactUri ->
                try {
                    val cursor = context.contentResolver.query(
                        contactUri,
                        arrayOf(
                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                        ),
                        null,
                        null,
                        null
                    )
                    cursor?.use {
                        if (it.moveToFirst()) {
                            val phoneIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

                            if (phoneIndex >= 0 && nameIndex >= 0) {
                                val newPhoneNumber = it.getString(phoneIndex) ?: ""
                                val newContactName = it.getString(nameIndex) ?: ""

                                // Sobrescribir el contacto actual
                                savedPhoneNumber = newPhoneNumber
                                savedContactName = newContactName

                                Toast.makeText(
                                    context,
                                    if (savedContactName.isNotEmpty()) "Contacto actualizado: $newContactName" else "Contacto guardado: $newContactName",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SOSView", "Error al obtener contacto", e)
                    Toast.makeText(context, "Error al obtener contacto", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Launcher para permisos de llamada
    val callPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            makePhoneCall(context, savedPhoneNumber)
        } else {
            Toast.makeText(context, "Permiso de llamada denegado", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(azulRey, azulCielo)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "EMERGENCIA SOS",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            // Botón guardar contacto
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_PICK).apply {
                        type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
                    }
                    contactPickerLauncher.launch(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = amarillo,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Seleccionar contacto",
                    tint = azulRey,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = if (savedContactName.isEmpty()) "SELECCIONAR CONTACTO" else "CAMBIAR CONTACTO",
                    color = azulRey,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Botón llamada emergencia
            OutlinedButton(
                onClick = {
                    if (savedPhoneNumber.isNotEmpty()) {
                        when (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)) {
                            PackageManager.PERMISSION_GRANTED -> {
                                makePhoneCall(context, savedPhoneNumber)
                            }
                            else -> {
                                callPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                            }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Primero selecciona un contacto de emergencia",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                    disabledContainerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.5f),
                    disabledContentColor = MaterialTheme.colorScheme.onError.copy(alpha = 0.5f)
                ),
                enabled = savedPhoneNumber.isNotEmpty()
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Llamar",
                    tint = androidx.compose.ui.graphics.Color.White,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "LLAMAR EMERGENCIA",
                    color = androidx.compose.ui.graphics.Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun makePhoneCall(context: android.content.Context, phoneNumber: String) {
    try {
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Log.e("SOSView", "Error al realizar llamada", e)
        Toast.makeText(context, "Error al realizar la llamada", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun SOSPreview() {
    SilverCareTheme {
        SOSframe()
    }
}