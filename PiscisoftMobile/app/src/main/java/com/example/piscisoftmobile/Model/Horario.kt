package com.example.piscisoftmobile.Model

import java.io.Serializable

//1
data class Horario(
    var diaSemana : String? = null,
    var horaInicio: String? = null,
    var horaFin: String? = null,
    var capacidadTotal: Int? = null,
    var codProfesor:Int?=null
): Serializable