package com.example.piscisoftmobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.piscisoftmobile.Model.Usuario
import com.example.piscisoftmobile.Model.UsuarioFirebase
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_modificar.*

class ModificarActivity : AppCompatActivity() {

    private lateinit var mContext: Context
    var niveles = mutableListOf<String>()
    var tipos = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modificar)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val but_cancelar = findViewById<Button>(R.id.btn_cancelar_modificar)
        but_cancelar.setOnClickListener { Regresar() }

        val userID = intent.getStringExtra("userID")
        val usuarioFirebase = UsuarioFirebase()
        usuarioFirebase.obtenerUsuarioById(this,userID!!)

        niveles.add("Básico")
        niveles.add("Intermedio")
        niveles.add("Avanzado")

        tipos.add("Alumno")
        tipos.add("Trabajador")
        tipos.add("Egresado")

    }

    fun ActualizarUsuario(usuario: Usuario){
        Toast.makeText(this, "Aun falta", Toast.LENGTH_SHORT).show()

        val db = FirebaseFirestore.getInstance()
        db.collection("usuario").document(usuario.codigo!!).set(usuario)
        Toast.makeText(this, "Modificacion realizada", Toast.LENGTH_SHORT).show()
        Regresar()
    }

    private fun vacio(et: EditText) : Boolean{
        if (et.text.toString().contentEquals("")) {
            return true
        }
        return false
    }

    private fun GuardarCambios(usuario: Usuario) {

        var nivel_usuario = nivel
        var tipo_usuairo = tipo
        var celular_usuaio = celular
        var observaciones_usuario = observaciones

        if (!vacio(celular_usuaio)||celular_usuaio.length()!=9){
            usuario.celular = celular_usuaio.text.toString()
        }

        usuario.tipo = tipo_usuairo.selectedItem.toString()
        usuario.nivel = nivel_usuario.selectedItem.toString()

        if (vacio(observaciones_usuario)){
            usuario.observaciones = "Ninguna"
        }else{
            usuario.observaciones = observaciones_usuario.text.toString()
        }

        ActualizarUsuario(usuario)
    }

    fun Regresar(){
        val intent = Intent()
        intent.setClass(this, SesionActivity::class.java)
        startActivity(intent)
    }

    fun OnUserDataFinished(usuario: Usuario) {
        nombre.text = usuario.nombre
        codigo.text = usuario.codigo
        celular.setText(usuario.celular)
        var tipo = findViewById<Spinner>(R.id.tipo)
        var nivel = findViewById<Spinner>(R.id.nivel)

        for ( i in niveles.indices){
            if (usuario.nivel.toString() == niveles[i])
                nivel.setSelection(i)
        }

        for ( i in tipos.indices){
            if (usuario.tipo.toString() == tipos[i])
                tipo.setSelection(i)
        }

        observaciones.setText(usuario.observaciones)

        val url = usuario.foto
        Picasso.get().load(url).resize(350,350).into(foto)

        val but_guardar = findViewById<Button>(R.id.btn_guardar_modificacion)
        but_guardar.setOnClickListener { GuardarCambios(usuario) }
    }

}
