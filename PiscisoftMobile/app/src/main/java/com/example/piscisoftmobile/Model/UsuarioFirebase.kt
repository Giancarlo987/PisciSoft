package com.example.piscisoftmobile.Model

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.piscisoftmobile.MainActivity
import com.example.piscisoftmobile.OnDataFinishedListener
import com.google.firebase.firestore.FirebaseFirestore
import com.example.piscisoftmobile.PerfilFragment
import com.example.piscisoftmobile.VerReservasProfesorActivity

class UsuarioFirebase  {
    val db = FirebaseFirestore.getInstance()
    val ref = db.collection("usuario")


    fun obtenerUsuarioById(listener: OnDataFinishedListener, userID:String){
        val query = ref.whereEqualTo("codigo",userID)
        query.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val usuario = document.toObject(Usuario::class.java)
                    listener.OnUserDataFinished(usuario)
                }
            }
            .addOnFailureListener{ exception ->
                Log.d("ERROR EN FIREBASE", "get failed with ", exception)
            }
    }

    fun verificarCredenciales(listener: OnDataFinishedListener,codigo:String,password:String)  {
        val query = ref.whereEqualTo("codigo",codigo).whereEqualTo("password",password)
        query.get()
            .addOnSuccessListener { documents ->
                if ( ! documents.isEmpty ) {
                    listener.OnVerificacionFinished(true)
                } else {
                    listener.OnVerificacionFinished(false)
                }
            }
            .addOnFailureListener{ exception ->
                Log.d("ERROR EN FIREBASE", "get failed with ", exception)
            }
    }

    fun verificarUsuarioHabilitado(listener:OnDataFinishedListener, codUsuario:String){
        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("usuario")

        val query = ref.whereEqualTo("codigo",codUsuario)
        query.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val usuario = document.toObject(Usuario::class.java)
                    if (usuario.estado!="Suspendido"){
                        listener.OnUsuarioHabilitadoFinished(true)
                    }else{
                        listener.OnUsuarioHabilitadoFinished(false)
                        //Toast.makeText(this, "No puede realizar reservas, se encuentra suspendido", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

}