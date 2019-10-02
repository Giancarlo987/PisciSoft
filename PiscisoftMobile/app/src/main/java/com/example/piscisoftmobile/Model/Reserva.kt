package com.example.piscisoftmobile.Model

import java.io.Serializable

data class Reserva (
    var codigoTurno : String? = null,
    var codigoUsuario : String? = null,
    var estado : String? = null,
    var fecha : String? = null,
    var modalidad : String? = null
): Serializable

