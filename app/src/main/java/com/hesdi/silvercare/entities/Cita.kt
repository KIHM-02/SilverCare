package com.hesdi.silvercare.entities

data class Cita(
    var id:String="",
    val medico: String="",
    val especializacion:String ="",
    val Lugar: String= "",
    val fecha:String="",
    val hora : String= "",
){
    fun toMap(): Map<String, String>{
        return mapOf(
            "medico" to medico,
            "especializacion" to especializacion,
            "lugar" to Lugar,
            "fecha" to fecha,
            "hora" to hora,
        )
    }
}