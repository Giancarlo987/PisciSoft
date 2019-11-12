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
import com.example.piscisoftmobile.Model.UsuarioFirebase
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detalle_reserva.*
import kotlinx.android.synthetic.main.fragment_perfil.*
import kotlinx.android.synthetic.main.fragment_ver_reserva_detalle.*
import java.net.URL


class PerfilFragment : Fragment(), OnDataFinishedListener {

    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_perfil, container, false)
        val btn_modificar: Button = root.findViewById(R.id.btn_modificar)


        val userID = obtenerUsuarioLogueado()
        val usuarioFirebase = UsuarioFirebase()
        usuarioFirebase.obtenerUsuarioById(this,userID!!)

        btn_modificar.setOnClickListener { irModificarActivity( userID) }

        mContext = root.context
        return root

    }

    override fun OnUserDataFinished(usuario: Usuario) {
        tv_nombre.text = usuario.nombre
        tv_codigo.text = usuario.codigo
        rv_inasistencias.rating = 3 - usuario.inasistencias!!.toFloat()
        tv_estado.text =  "Estado: ${usuario.estado!!.toUpperCase()}"
        tv_tipo.text = "Tipo de usuario: ${usuario.tipo}"
        tv_nivel.text = "Nivel de natación: ${usuario.nivel}"
        tv_observaciones.text = "Observaciones: ${usuario.observaciones}"
        val url = usuario.foto
        Picasso.get().load(url).resize(350,350).into(iv_foto)
    }

    private fun obtenerUsuarioLogueado():String?{
        val sharedPreferences : SharedPreferences = requireActivity().getSharedPreferences("login",Context.MODE_PRIVATE)
        var userID = sharedPreferences.getString("userID","")
        //Toast.makeText( context, userID, Toast.LENGTH_SHORT).show()
        return userID
    }


    private fun irModificarActivity(userID : String){
        val intent = Intent()
        intent.putExtra("userID",userID)
        intent.setClass(mContext, ModificarActivity::class.java)
        startActivity(intent)
    }
}