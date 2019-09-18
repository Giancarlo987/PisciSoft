package com.example.piscisoftmobile.Model

import java.io.Serializable


data class Usuario(
    var nombre: String? = "",
    var codigo: String? = "",
    var celular: String? = "",
    var estado: String? = "",
    var inasistencias: Number? = null,
    var nivel: String? = "",
    var password: String? = "",
    var tipo: String? = "",
    var foto: String? = ""
): Serializable
