package com.example.piscisoftmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        btn_registrarse.setOnClickListener { registrar() }
    }

    private fun registrar(){
        //Extraer todos los inputs del usuario
        //Verificar
        //Agregar nuevo usuario a Firebase



        //Volver al login
        regresar()
    }

    private fun regresar(){
        val intent = Intent()
        intent.setClass(this, MainActivity::class.java)
        startActivityForResult(intent,1)
    }
}
