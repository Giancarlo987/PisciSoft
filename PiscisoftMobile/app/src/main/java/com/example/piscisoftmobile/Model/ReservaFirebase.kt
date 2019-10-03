package com.example.piscisoftmobile.Model

import android.util.Log
import com.example.piscisoftmobile.HistorialFragment
import com.google.firebase.firestore.FirebaseFirestore

class ReservaFirebase {

    val db = FirebaseFirestore.getInstance()
    val ref = db.collection("reserva")

    fun existenReservas(fragment: HistorialFragment, codigo:String){
        val query = ref.whereEqualTo("codUsuario",codigo)
        query.get()
            .addOnSuccessListener { documents ->
                if ( ! documents.isEmpty ) {
                    fragment.irReservas(true)
                } else {
                    fragment.irReservas(false)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("ERROR FIREBASE", "Error getting documents: ", exception)
            }
    }

    fun retornarReservas(fragment: HistorialFragment, codigo:String){
        val query = ref.whereEqualTo("codUsuario",codigo)
        query.get()
            .addOnSuccessListener { documents ->
                var reservas = mutableListOf<Reserva>()
                for (document in documents) {
                    val reserva = document.toObject(Reserva::class.java)
                    reservas.add(reserva)
                }

                //reservas.sortBy { reserva -> reserva.fecha?.substring(reserva.fecha?.indexOf("-")!! + 1, reserva.fecha?.indexOf("-")!!)!!;}
                fragment.setRecyclerAdapter(reservas)
            }
            .addOnFailureListener { exception ->
                Log.w("ERROR FIREBASE", "Error getting documents: ", exception)
            }
    }


}
