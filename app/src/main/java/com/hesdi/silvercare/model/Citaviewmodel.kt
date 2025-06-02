package com.hesdi.silvercare.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.hesdi.silvercare.entities.Cita
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class Citaviewmodel: ViewModel() {
    private val db= Firebase.firestore
    private var searchJob: Job? = null

    private var Lista_citas= MutableStateFlow<List<Cita>>(emptyList())
    val listaCitas = Lista_citas.asStateFlow()

    init {
        obtenerCitas()
    }

    fun obtenerCitas() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = db.collection("agenda")
                    .whereEqualTo("userId", uid)
                    .get()
                    .await()

                val citas = result.documents.mapNotNull {
                    it.toObject(Cita::class.java)
                }
                Lista_citas.value = citas
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun agregarCita(cita: Cita){
        cita.id= UUID.randomUUID().toString()
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("agenda").document(cita.id).set(cita)
                .addOnSuccessListener {
                    obtenerCitas()
                }
        }
    }
    fun buscarPorEspecialidadYUsuario(especialidad: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = db.collection("agenda")
                    .whereEqualTo("especializacion", especialidad)
                    .whereEqualTo("userId", uid)
                    .get()
                    .await()

                val citasActualizadas = result.documents.mapNotNull {
                    it.toObject(Cita::class.java)
                }
                Lista_citas.value = citasActualizadas
            } catch (e: Exception) {
                Log.e("Firebase", "Error al buscar por especialidad y usuario: ${e.message}")
            }
        }
    }



    fun actualizarCita(cita: Cita){
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("agenda").document(cita.id).update(cita.toMap())
                .addOnSuccessListener {
                    obtenerCitas()
                }
        }
    }

    fun borrarCita(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("agenda").document(id).delete()
                .addOnSuccessListener {
                    Lista_citas.value=listaCitas.value.filter {
                        it.id != id
                    }
                }
        }

    }
}