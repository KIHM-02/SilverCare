package com.hesdi.silvercare.entities

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class Medicamento(
    /*
    private var nombre: String = "",
    private var imgUrl: String = "",
    private var formato: String = "",
    private var periodo: Int = 0,
    private var hora: String = "",
    private var userId: String = ""
    */
) {

    private val db = Firebase.firestore

    fun insertData(
        nombre: String,
        imgUrl: String,
        formato: String,
        periodo: Int,
        hora: String,
        userId: String
    ): Boolean {
        return try {
            val medicamento = db.collection("Medicamento")

            val data = hashMapOf(
                "nombre" to nombre,
                "imgUrl" to imgUrl,
                "formato" to formato,
                "periodo" to periodo,
                "hora" to hora,
                "userId" to userId
            )

            medicamento.add(data) // Esto genera un ID único automáticamente
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


}