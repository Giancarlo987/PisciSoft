package com.example.piscisoftmobile.Model

import java.io.Serializable


data class Turno(
    var codHorario: String? = null,
    var fecha : String? = null,
    var capacidadCubierta: Int? = null,
    var abierto: Boolean? = null,
    var observaciones: String? = null
): Serializable