package com.example.piscisoftmobile

import com.example.piscisoftmobile.Model.Turno
import com.example.piscisoftmobile.Model.Usuario

interface OnDataFinishedListener {
    fun OnUserDataFinished(data : Usuario) {}
    fun OnVerificacionFinished(existe : Boolean) {}
    fun OnListaTurnosDataFinished(listaTurnos : List<Turno>) {}
    fun OnActualizacionFinished() {}
    fun OnUsuarioHabilitadoFinished(habilitado : Boolean) {}
    fun OnRegistroReservaFinished() {}
}