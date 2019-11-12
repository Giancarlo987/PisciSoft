package com.example.piscisoftmobile.Model

import android.util.Log
import android.widget.Toast
import com.example.piscisoftmobile.HistorialFragment
import com.example.piscisoftmobile.OnDataFinishedListener
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ReservaFirebase {

    val db = FirebaseFirestore.getInstance()
    val ref = db.collection("reserva")
    val turnoFirebase = TurnoFirebase()

    fun existeReservaEsteDia(listener:OnDataFinishedListener, codUsuario: String, codTurno: String){ //Verificar si existe reserva este día
        var fechaANoRepetir = codTurno.substring(0, 10)
        val query = ref.whereEqualTo("codUsuario",codUsuario).whereEqualTo("estado","Pendiente")
        query.get()
            .addOnSuccessListener { documents ->
                var existe = false
                for (document in documents) {
                    val reserva = document.toObject(Reserva::class.java)
                    if (reserva.codTurno!!.contains(fechaANoRepetir,false)){
                        existe = true
                        break
                    }
                }
                listener.OnVerificacionFinished(existe)
            }
    }

    fun registrarReserva(listener: OnDataFinishedListener, reserva: Reserva){
        val db = FirebaseFirestore.getInstance()
        db.collection("reserva").add(reserva)
        turnoFirebase.actualizarCapacidad(reserva.codTurno!!, "Disminuir")
        listener.OnRegistroReservaFinished()
    }
    

    fun obtenerReservasByUsuario(listener: OnDataFinishedListener, codigo:String){
        val query = ref.whereEqualTo("codUsuario",codigo)
        query.get()
            .addOnSuccessListener { documents ->
                var reservas = mutableListOf<Reserva>()
                for (document in documents) {
                    val reserva = document.toObject(Reserva::class.java)
                    reserva.codReserva = document.id
                    reservas.add(reserva)
                }
                //reservas.sortBy { reserva -> reserva.fecha?.substring(reserva.fecha?.indexOf("-")!! + 1, reserva.fecha?.indexOf("-")!!)!!;}
                listener.OnListaReservasDataFinished(reservas)
            }
            .addOnFailureListener { exception ->
                Log.w("ERROR FIREBASE", "Error getting documents: ", exception)
            }
    }

    fun actualizarReservas(codigo:String){
        val query = ref.whereEqualTo("codUsuario",codigo)
        query.get()
            .addOnSuccessListener { documents ->
                val reservasAEvaluar = mutableListOf<Reserva>()
                for (document in documents) {
                    val reserva = document.toObject(Reserva::class.java)
                    if (reserva.estado=="Pendiente"){
                        reserva.codReserva = document.id
                        reservasAEvaluar.add(reserva)
                    }
                }
                verificarReservaInasistida(reservasAEvaluar)

            }
            .addOnFailureListener { exception ->
                Log.w("ERROR FIREBASE", "Error getting documents: ", exception)
            }
    }

    fun verificarReservaInasistida(reservasAEvaluar:MutableList<Reserva>){
        for (reserva in reservasAEvaluar){
            val query = db.collection("turno").document(reserva.codTurno!!)
            query.get()
                .addOnSuccessListener { document ->
                    var turno = document.toObject(Turno::class.java)
                    if (reservaInasistida(turno!!.horaInicio!!, turno!!.fecha!!)){
                        ref.document(reserva.codReserva!!).update("estado","Inasistida")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("ERROR FIREBASE", "Error getting documents: ", exception)
                }
        }
    }

    fun reservaInasistida(horaInicio:String,fechaTurno:String):Boolean{

        val hoy = LocalDate.now()
        val formato = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val fechaTurno = LocalDate.parse(fechaTurno, formato)

        if (fechaTurno.isEqual(hoy)) {

            var horaTurno = horaInicio
            if (horaTurno.length == 4) {
                horaTurno = "0${horaTurno}"
            }

            val horaActual = LocalTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val horaAEvaluar = LocalTime.parse(horaTurno, formatter)

            if (horaActual.isAfter(horaAEvaluar)) {
                return true
            }
            return false
        }
        else {
            if (hoy.isAfter(fechaTurno)){
                return true
            }
            return false
        }

    }


    fun obtenerReservasEnTurno(listener:OnDataFinishedListener, codTurno:String){
        val db = FirebaseFirestore.getInstance()
        db.collection("reserva").whereEqualTo("codTurno",codTurno)
            .get()
            .addOnSuccessListener { documents ->
                var reservas = mutableListOf<Reserva>()
                for (document in documents){
                    val reserva = document.toObject(Reserva::class.java)
                    reservas.add(reserva)
                }
                listener.OnListaReservasDataFinished(reservas)
            }
            .addOnFailureListener{ exception ->
                Log.d("ERROR EN FIREBASE", "get failed with ", exception)
            }
    }

    fun cancelarReserva(justificada:Boolean,reserva:Reserva){
        if (justificada){
            ref.document(reserva.codReserva!!).update("estado","Justificada")
        } else {
            ref.document(reserva.codReserva!!).update("estado","Cancelada")
        }
        turnoFirebase.actualizarCapacidad(reserva.codTurno!!, "Aumentar")
    }

}
