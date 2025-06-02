package com.hesdi.silvercare.entities

data class Cita(
    var id:String="",
    val medico: String="",
    val especializacion:String ="",
    val lugar: String= "",
    val fecha:String="",
    val hora : String= "",
    val userId: String = ""
){
    fun toMap(): Map<String, String>{
        return mapOf(
            "medico" to medico,
            "especializacion" to especializacion,
            "lugar" to lugar,
            "fecha" to fecha,
            "hora" to hora,
            "userId" to userId,
        )
    }
}