package com.example.piscisoftmobile.Model

import android.util.Log
import android.widget.Toast
import com.example.piscisoftmobile.OnDataFinishedListener
import com.example.piscisoftmobile.ReservarFragment
import com.example.piscisoftmobile.ReservasRecyclerAdapter
import com.example.piscisoftmobile.TurnosActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TurnoFirebase {

    val db = FirebaseFirestore.getInstance()
    val ref = db.collection("turno")

    fun existenTurnosRegistrados(listener: OnDataFinishedListener, fecha:String){
        val query = ref.whereEqualTo("fecha",fecha)
        query.get()
            .addOnSuccessListener { documents ->
                if ( ! documents.isEmpty ) {
                    listener.OnVerificacionFinished(true)
                } else {
                    listener.OnVerificacionFinished(false)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("ERROR FIREBASE", "Error getting documents: ", exception)
            }
    }

    fun obtenerTurnosByFecha(listener: OnDataFinishedListener, fecha:String){
        val query = ref.whereEqualTo("fecha",fecha)
        query.get()
            .addOnSuccessListener { documents ->
                var turnos = mutableListOf<Turno>()
                for (document in documents) {
                    var turno = document.toObject(Turno::class.java)
                    turno.id = document.id
                    turnos.add(turno)
                }
                turnos.sortBy { turno -> turno.horaInicio?.substring(0, turno.horaInicio?.indexOf(":")!!)!!.toInt()-1;}
                listener.OnListaTurnosDataFinished(turnos)
            }
            .addOnFailureListener { exception ->
                Log.w("ERROR FIREBASE", "Error getting documents: ", exception)
            }
    }

    fun actualizarTurnos(listener: OnDataFinishedListener, fecha:String){
        val query = ref.whereEqualTo("fecha",fecha)
        query.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var turno = document.toObject(Turno::class.java)
                    if (turnoCaducado(turno.horaInicio!!,turno.fecha!!)){
                        ref.document(document.id).update("estado","Caducado")
                    } else {
                        if (turnoLleno(turno.capacidadTotal!! - turno.capacidadCubierta!!)){
                            ref.document(document.id).update("estado","Cerrado")
                            ref.document(document.id).update("observaciones","Turno lleno")
                        }
                    }
                }
                listener.OnActualizacionFinished()
            }
    }

    fun turnoLleno(diferencia:Int):Boolean{
        if (diferencia==0){
            return true
        }
        return false
    }

    fun turnoCaducado(horaInicio:String,fechaTurno:String):Boolean{

        val hoy = LocalDate.now()
        val formato = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val fechaEscogida = LocalDate.parse(fechaTurno, formato)

        if (fechaEscogida.isEqual(hoy)) {

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
        return false

    }

    fun obtenerTurnoByCodigo(listener: OnDataFinishedListener, codTurno:String, holder: ReservasRecyclerAdapter.ViewHolder, position: Int, reserva:Reserva){
        val query = ref.document(codTurno)
        query.get()
            .addOnSuccessListener { document ->
                var turno = document.toObject(Turno::class.java)
                turno!!.id = document.id
                listener.OnTurnoDataFinished(turno, holder, position, reserva)
            }
            .addOnFailureListener { exception ->
                Log.w("ERROR FIREBASE", "Error getting documents: ", exception)
            }
    }

}