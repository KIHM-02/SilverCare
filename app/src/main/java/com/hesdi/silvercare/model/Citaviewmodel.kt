package com.hesdi.silvercare.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.hesdi.silvercare.entities.Cita
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class Citaviewmodel: ViewModel() {
    private val db= Firebase.firestore

    private var Lista_citas= MutableStateFlow<List<Cita>>(emptyList())
    val listaCitas = Lista_citas.asStateFlow()

    init {
        obtenerCitas()
    }

    fun obtenerCitas() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = db.collection("agenda").get().await()
                val citas = result.documents.mapNotNull {
                    it.toObject(Cita::class.java)
                }
                Lista_citas.value = citas
            } catch (e: Exception) {
                e.printStackTrace() // útil para ver el error en Logcat
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