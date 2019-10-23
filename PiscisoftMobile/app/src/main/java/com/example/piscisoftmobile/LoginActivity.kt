package com.example.piscisoftmobile

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.example.piscisoftmobile.Model.Horario
import com.example.piscisoftmobile.Model.Turno
import com.example.piscisoftmobile.Model.UsuarioFirebase
import kotlinx.android.synthetic.main.activity_detalle_reserva.*
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime


class MainActivity : AppCompatActivity(), OnDataFinishedListener  {

    val usuarioFirebase = UsuarioFirebase()
    lateinit var codigo : String
    lateinit var password : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_registrarse.setOnClickListener{irARegistroActivity()}
        btn_iniciar_sesion.setOnClickListener {verificarCampos()}
    }


    private fun verificarCampos(){
        codigo = et_codigo.text.toString()
        password = et_password.text.toString()

        if (codigo != "" && password != ""){
            usuarioFirebase.verificarCredenciales(this, codigo, password)
        } else {
            Toast.makeText(this, "Por favor, complete los campos", Toast.LENGTH_SHORT).show()
        }
    }

    override fun OnVerificacionFinished(existe: Boolean) {
        if (existe){
            val editor : SharedPreferences.Editor = getSharedPreferences("login",
                Context.MODE_PRIVATE).edit()
            editor.putString("userID",codigo)
            editor.apply()
            irAPerfilActivity()
        } else {
            Toast.makeText(this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun irAPerfilActivity(){
        val intent = Intent()
        intent.setClass(this, SesionActivity::class.java)
        startActivityForResult(intent,1)
    }

    private fun irARegistroActivity(){
        val intent = Intent()
        intent.setClass(this, RegistroActivity::class.java)
        startActivityForResult(intent,1)
    }

    //Esta función es para crear los turnos (no tocar):
    fun crearDatos(){

        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("turno")

        var turno = Turno()
        ref.add(turno)

        for (dia in 21..26){
            turno.fecha = "2019-10-${dia}"
            var date = LocalDate.parse(turno.fecha)

            var lim = 19
            if (date.dayOfWeek.toString() == "SATURDAY"){
                lim = 12
            }

            for (hora in 5..lim){
                turno.horaInicio="${hora}:30"
                turno.horaFin="${hora+1}:15"
                turno.capacidadCubierta = 0
                turno.capacidadTotal = 16
                turno.estado = "Abierto"
                if (hora in 5..8){
                    turno.profesor = 100
                } else if (hora in 9..12){
                    turno.profesor = 200
                } else if (hora in 13..16){
                    turno.profesor = 300
                } else if (hora in 17..19){
                    turno.profesor = 400
                }
                turno.id = turno.fecha +"."+ turno.horaInicio
                ref.document(turno.fecha +"."+ turno.horaInicio).set(turno)

            }
        }

    }
}
