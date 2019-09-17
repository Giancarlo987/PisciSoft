package com.example.piscisoftmobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.util.Log


class Texto ( val textox: String = "" )

class MainActivity : AppCompatActivity() {

    //**************************************************************
    // Inicio prueba de arquitectura
    //**************************************************************
    private fun actualizar(t1: Texto) {
        try {
            val db = FirebaseFirestore.getInstance()
            val ref = db.collection("colx").document("docx")
            ref.update("textox", t1.textox)
            Toast.makeText(this, "Texto actualizado", Toast.LENGTH_SHORT).show()
            //Task successful
        } catch (e: Throwable) {
            Toast.makeText(this, "Error en la actualización: ${e}", Toast.LENGTH_SHORT).show()
            //Manage error
        }
    }

    private fun ver() {
        try {
            val db = FirebaseFirestore.getInstance()
            val ref = db.collection("colx").document("docx")
            ref.get().addOnSuccessListener { documentSnapshot ->
                val t1 = documentSnapshot.toObject(Texto::class.java)
                textView.text = t1?.textox
                Toast.makeText(this, "Texto recuperado", Toast.LENGTH_SHORT).show()
            }
            //Task successful
        } catch (e: Throwable) {
            Toast.makeText(this, "Error en la visualización: ${e}", Toast.LENGTH_SHORT).show()
            //Manage error
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ver()

        btn_actualizar.setOnClickListener {
            if (editText.text.toString() != ""){
                val t1 = Texto(editText.text.toString())
                actualizar(t1)
                ver()

            } else {
                Toast.makeText(this, "Ingrese un nuevo texto!!!!", Toast.LENGTH_SHORT).show()
            }
        }

        //**************************************************************
        // Fin prueba de arquitectura
        //**************************************************************


        //Ir al formulario de registro
        btn_registrarse.setOnClickListener{irRegistro()}


        //Ir al dashboard del usuario luego de que inició sesión
        btn_iniciar_sesion.setOnClickListener {iniciarSesion()}

    }

    private fun irRegistro(){ //Solo va al formulario de registro
        Toast.makeText(this, "Le diste click a registro xd", Toast.LENGTH_SHORT).show()

        val intent : Intent = Intent()
        intent.setClass(this, RegistroActivity::class.java)
        startActivityForResult(intent,1)

    }

    private fun verificar(codigo:String,password:String)  {

            val db = FirebaseFirestore.getInstance()
            val ref = db.collection("usuario")
            val query = ref.whereEqualTo("codigo",codigo).whereEqualTo("password",password)

            query.get()
                .addOnSuccessListener { documents ->
                    if ( ! documents.isEmpty ) {
                        Toast.makeText(this, "Inicio exitoso", Toast.LENGTH_SHORT).show()
                        irPerfil(codigo)
                    } else {
                        Toast.makeText(this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener{
                        Toast.makeText(this, "Error en Firebase", Toast.LENGTH_SHORT).show()
                }


    }

    private fun iniciarSesion(){ //Verificar credenciales -> Ir al perfil

        //Verificamos que estén llenos los campos
        val codigo = et_codigo.text.toString()
        val password = et_password.text.toString()

        if (codigo != "" && password != ""){
            //Toast.makeText(this, "Completó los campos", Toast.LENGTH_SHORT).show()
            verificar(codigo,password)

        } else {
            Toast.makeText(this, "Por favor, complete los campos", Toast.LENGTH_SHORT).show()

        }


    }

    private fun irPerfil(codigo:String){
        //to-do
        val intent : Intent = Intent()
        intent.putExtra("codigo",codigo)
        intent.setClass(this, SesionActivity::class.java)
        startActivityForResult(intent,1)

    }
}
