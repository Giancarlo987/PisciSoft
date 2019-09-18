package com.example.piscisoftmobile.views

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.content.SharedPreferences
import com.example.piscisoftmobile.R


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_registrarse.setOnClickListener{irRegistro()}
        btn_iniciar_sesion.setOnClickListener {iniciarSesion()}

    }

    private fun irRegistro(){ //Dirigirse al formulario de registro
        val intent = Intent()
        intent.setClass(this, RegistroActivity::class.java)
        startActivityForResult(intent,1)
    }


    private fun iniciarSesion(){
        val codigo = et_codigo.text.toString()
        val password = et_password.text.toString()

        if (codigo != "" && password != ""){ //Verificar si los campos están llenos
            verificar(codigo,password)
        } else {
            Toast.makeText(this, "Por favor, complete los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verificar(codigo:String,password:String)  {

        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("usuario")
        val query = ref.whereEqualTo("codigo",codigo).whereEqualTo("password",password)

        query.get()
            .addOnSuccessListener { documents ->
                if ( ! documents.isEmpty ) {
                    Toast.makeText(this, "Inicio exitoso", Toast.LENGTH_SHORT).show()

                    //"Guardar en la sesión"
                    val editor : SharedPreferences.Editor = getSharedPreferences("login",
                        Context.MODE_PRIVATE).edit()
                    editor.putString("userID",codigo)
                    editor.apply()

                    irPerfil(codigo)

                } else {
                    Toast.makeText(this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{
                Toast.makeText(this, "Error en Firebase", Toast.LENGTH_SHORT).show()
            }
    }

    private fun irPerfil(codigo:String){

        val intent = Intent()
        intent.putExtra("codigo",codigo)
        intent.setClass(this, SesionActivity::class.java)
        startActivityForResult(intent,1)

    }
}
