package com.example.piscisoftmobile

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.content.SharedPreferences
import com.example.piscisoftmobile.Model.UsuarioFirebase


class MainActivity : AppCompatActivity() {

    val usuarioFirebase = UsuarioFirebase()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_registrarse.setOnClickListener{irRegistro()}
        btn_iniciar_sesion.setOnClickListener {verificarCampos()}

    }

    private fun irRegistro(){ //Dirigirse al formulario de registro
        val intent = Intent()
        intent.setClass(this, RegistroActivity::class.java)
        startActivityForResult(intent,1)
    }


    private fun verificarCampos(){
        val codigo = et_codigo.text.toString()
        val password = et_password.text.toString()

        if (codigo != "" && password != ""){
            usuarioFirebase.verificarUsuario(this, codigo, password)
        } else {
            Toast.makeText(this, "Por favor, complete los campos", Toast.LENGTH_SHORT).show()
        }
    }

    fun iniciarSesion(codigo:String,mensaje:String){
        if (mensaje=="existe"){
            //"Guardamos en la sesión"
            val editor : SharedPreferences.Editor = getSharedPreferences("login",
                Context.MODE_PRIVATE).edit()
            editor.putString("userID",codigo)
            editor.apply()
            irPerfil()
        }else{
            Toast.makeText(this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
    }


    private fun irPerfil(){

        val intent = Intent()
        intent.setClass(this, SesionActivity::class.java)
        startActivityForResult(intent,1)

    }
}
