package com.example.piscisoftmobile.Model


data class Usuario(
    var codigo: String? = null,
    var estado: String? = null,
    var password: String? = null,
    var tipo: String? = null,
    var observaciones: String? = null,
    var celular: String? = null,
    var inasistencias: Long? = null,
    var nivel: String? = null,
    var nombre: String? = null
)
