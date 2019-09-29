package com.example.piscisoftmobile.Model

import android.util.Log
import androidx.fragment.app.Fragment
import com.example.piscisoftmobile.MainActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.example.piscisoftmobile.PerfilFragment

//Código que se conecta a la base de datos

class UsuarioFirebase  {
    val db = FirebaseFirestore.getInstance()
    val ref = db.collection("usuario")


    fun retornarUsuario(fragment: PerfilFragment, userID:String){
        val query = ref.whereEqualTo("codigo",userID)
        query.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val usuario = document.toObject(Usuario::class.java)
                    fragment.colocarInfo(usuario)
                }
            }
            .addOnFailureListener{ exception ->
                Log.d("ERROR EN FIREBASE", "get failed with ", exception)
            }
    }

    fun verificarUsuario(activity:MainActivity,codigo:String,password:String)  {

        val query = ref.whereEqualTo("codigo",codigo).whereEqualTo("password",password)

        query.get()
            .addOnSuccessListener { documents ->
                if ( ! documents.isEmpty ) {
                    activity.iniciarSesion(codigo,"existe")
                } else {
                    activity.iniciarSesion(codigo,"no existe")
                }
            }
            .addOnFailureListener{ exception ->
                Log.d("ERROR EN FIREBASE", "get failed with ", exception)
            }

    }

}