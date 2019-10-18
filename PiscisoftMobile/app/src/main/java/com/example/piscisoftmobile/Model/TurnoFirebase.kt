package com.example.piscisoftmobile.Model

import android.util.Log
import android.widget.Toast
import com.example.piscisoftmobile.ReservarFragment
import com.example.piscisoftmobile.TurnosActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TurnoFirebase {

    val db = FirebaseFirestore.getInstance()
    val ref = db.collection("turno")

    fun existenTurnos(fragment: ReservarFragment, fecha:String){
        val query = ref.whereEqualTo("fecha",fecha)
        query.get()
            .addOnSuccessListener { documents ->
                if ( ! documents.isEmpty ) {
                    fragment.irTurnos(true)
                } else {
                    fragment.irTurnos(false)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("ERROR FIREBASE", "Error getting documents: ", exception)
            }
    }

    fun retornarTurnos(activity:TurnosActivity, fecha:String){
        val query = ref.whereEqualTo("fecha",fecha)
        query.get()
            .addOnSuccessListener { documents ->
                var turnos = mutableListOf<Turno>()
                for (document in documents) {
                    var turno = document.toObject(Turno::class.java)
                    evaluarTurno(turno,document.id)

                    turno = document.toObject(Turno::class.java)
                    turno.id = document.id
                    turnos.add(turno)
                }

                turnos.sortBy { turno -> turno.horaInicio?.substring(0, turno.horaInicio?.indexOf(":")!!)!!.toInt()-1;}
                activity.setRecyclerAdapter(turnos)
            }
            .addOnFailureListener { exception ->
                Log.w("ERROR FIREBASE", "Error getting documents: ", exception)
            }
    }

    fun evaluarTurno(turno:Turno,id:String){

        if (turnoCaducado(turno.horaInicio!!)){
            ref.document(id).update("estado","Caducado")
        }
        if (turnoLleno(turno.capacidadTotal!! - turno.capacidadCubierta!!)){
            ref.document(id).update("estado","Cerrado")
            ref.document(id).update("observaciones","Turno lleno")
        }
        
    }

    fun turnoLleno(diferencia:Int):Boolean{
        if (diferencia==0){
            return true
        }
        return false
    }

    fun turnoCaducado(horaInicio:String):Boolean{

        var horaTurno = horaInicio
        if (horaTurno.length==4){
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


}