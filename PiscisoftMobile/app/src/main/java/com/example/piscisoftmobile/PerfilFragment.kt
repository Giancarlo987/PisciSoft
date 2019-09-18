package com.example.piscisoftmobile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.piscisoftmobile.Model.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_perfil.*


class PerfilFragment : Fragment() {


    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_perfil, container, false)
        val btn_modificar: Button = root.findViewById(R.id.btn_modificar)


        //Obtener el usuario que se logueo
        val codigo = savedInstanceState?.get("codigo")
        val sharedPreferences : SharedPreferences = requireActivity().getSharedPreferences("login",Context.MODE_PRIVATE)
        var userID = sharedPreferences.getString("userID","")
        Toast.makeText( context, userID, Toast.LENGTH_SHORT).show()

        verInfoUsuario(userID)


        btn_modificar.setOnClickListener { ir_modificar() }
        mContext = root.context
        return root

    }


    private fun verInfoUsuario(userID : String?){
        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("usuario")
        val query = ref.whereEqualTo("codigo",userID)

        query.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val datos = document.data
                    tv_nombre.text = datos["nombre"].toString()
                    tv_codigo.text = datos["codigo"].toString()
                    rv_inasistencias.rating = datos["inasistencias"].toString().toFloat()
                    tv_estado.text = datos["estado"].toString()
                    tv_tipo.text = datos["tipo"].toString()
                    tv_nivel.text = datos["nivel"].toString()
                    tv_observaciones.text = datos["observaciones"].toString()
                }
            }
            .addOnFailureListener{
                Toast.makeText(context, "Error en Firebase", Toast.LENGTH_SHORT).show()
            }


    }


    private fun ir_modificar(){
        val intent : Intent = Intent()
        intent.setClass(mContext, ModificarActivity::class.java)
        startActivity(intent)
    }
}