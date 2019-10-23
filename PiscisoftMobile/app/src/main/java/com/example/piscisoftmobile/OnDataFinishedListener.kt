package com.example.piscisoftmobile

import com.example.piscisoftmobile.Model.Reserva
import com.example.piscisoftmobile.Model.Turno
import com.example.piscisoftmobile.Model.Usuario

interface OnDataFinishedListener {
    fun OnUserDataFinished(data : Usuario) {}
    fun OnVerificacionFinished(existe : Boolean) {}
    fun OnListaTurnosDataFinished(listaTurnos : List<Turno>) {}
    fun OnActualizacionFinished() {}
    fun OnUsuarioHabilitadoFinished(habilitado : Boolean) {}
    fun OnRegistroReservaFinished() {}
    fun OnListaReservasDataFinished(listaReservas : List<Reserva>) {}
    fun OnTurnoDataFinished(turno:Turno, holder: ReservasRecyclerAdapter.ViewHolder, position: Int, reserva:Reserva) {}
    fun OnUserNombreDataFinished(nombreUsuario : String) {}
    fun OnUserDataFinished(usuario:Usuario, holder: ReservasProfesorRecyclerAdapter.ViewHolder, position: Int, reserva:Reserva) {}
}