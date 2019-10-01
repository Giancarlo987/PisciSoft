package com.example.piscisoftmobile.Model

import android.util.Log
import android.widget.Toast
import com.example.piscisoftmobile.ReservarFragment
import com.example.piscisoftmobile.TurnosActivity
import com.google.firebase.firestore.FirebaseFirestore

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
                    val turno = document.toObject(Turno::class.java)
                    turnos.add(turno)
                }
                activity.setRecyclerAdapter(turnos)
            }
            .addOnFailureListener { exception ->
                Log.w("ERROR FIREBASE", "Error getting documents: ", exception)
            }
    }


}