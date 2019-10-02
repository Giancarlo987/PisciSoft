package com.example.piscisoftmobile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.piscisoftmobile.Model.Reserva
import com.example.piscisoftmobile.Model.Turno
import com.example.piscisoftmobile.Model.Usuario
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_confirmar_reserva.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ConfirmarReservaActivity : AppCompatActivity() {

    private lateinit var codTurno : String
    private lateinit var codUsuario : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmar_reserva)

        codTurno = intent.getStringExtra("codTurno")
        val fecha = intent.getStringExtra("fecha")
        val horario = intent.getStringExtra("horario")
        val profesor = intent.getStringExtra("profesor")
        codUsuario = intent.getStringExtra("userID")

        var date = LocalDate.parse(fecha)

        tv_fecha.text = "Fecha: "+date.dayOfMonth+"/"+date.monthValue+"/"+date.year
        tv_horario.text = "Horario: "+horario
        tv_profesor.text = "Profesor(a): "+profesor

        //Toast.makeText(this, codUsuario , Toast.LENGTH_SHORT).show()

        btn_confirmar.setOnClickListener { verificarUsuario(codUsuario) }

    }

    /**
     *
     * data class Reserva(
    var codTurno:String? = null,
    var codUsuario:String? = null,
    var fechaReserva:String? = null,
    var modalidad: String? = null,
    var estado : String? = null
    ): Serializable
     */

    fun verificarUsuario(codUsuario:String){
        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("usuario")

        val query = ref.whereEqualTo("codigo",codUsuario)
        query.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val usuario = document.toObject(Usuario::class.java)
                    if (usuario.estado!="Suspendido"){
                        verificarReservas(codUsuario)
                    }else{
                        Toast.makeText(this, "No puede realizar reservas, se encuentra suspendido", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    fun camposVacios(): Boolean {
        if (!rb_1.isChecked && !rb_2.isChecked){
            Toast.makeText(this, "Por favor, ingrese la modalidad", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    fun procesarReserva(){
        if (camposVacios()==false){
            var modalidad = ""
            if (rb_1.isChecked){
                modalidad = "Aprendizaje y mejora"
            }else {
                modalidad = "Práctica libre"
            }
            writeReserva(modalidad)
        }
    }

    fun writeReserva(modalidad:String){

        var reserva = Reserva()
        reserva.codTurno = this.codTurno
        reserva.codUsuario = this.codUsuario
        reserva.modalidad = modalidad
        val formatter = DateTimeFormatter. ofPattern("yyyy-MM-dd")
        val current = LocalDateTime.now()
        val formatted = current. format(formatter)
        reserva.fechaReserva = formatted
        reserva.estado = "Pendiente"

        val db = FirebaseFirestore.getInstance()
        db.collection("reserva").add(reserva)

        actualizarCapacidad(reserva.codTurno!!)
        Toast.makeText(this, "¡Reserva realizada!", Toast.LENGTH_SHORT).show()
        irPerfil()

    }

    //Verificar si el usuario ya a realizado una reserva ese día
    fun verificarReservas(codUsuario: String){
        var fechaANoRepetir = this.codTurno.substring(0, 10)
        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("reserva")
        val query = ref.whereEqualTo("codUsuario",codUsuario)
        query.get()
            .addOnSuccessListener { documents ->
                var existe = false
                for (document in documents) {
                    val reserva = document.toObject(Reserva::class.java)
                    if (reserva.codTurno!!.contains(fechaANoRepetir,false)){
                        Toast.makeText(this, "Ya cuenta con reservas para este día", Toast.LENGTH_SHORT).show()
                        existe = true
                        break
                    }
                }
                if (existe == false){
                    procesarReserva()
                }
            }
    }



    fun actualizarCapacidad(codTurno:String){
        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("turno").document(codTurno)
        val capacidadTotal = intent.getIntExtra("capacidadTotal",0)

        ref.get()
            .addOnSuccessListener { document ->
                val turno = document.toObject(Turno::class.java)
                ref.update("capacidadCubierta", turno!!.capacidadCubierta!!+1)

                if (turno!!.capacidadCubierta!!+1 == capacidadTotal){
                    ref.update("abierto", false)
                    ref.update("observaciones", "No se cuenta con capacidad")
                }
            }
    }

    private fun irPerfil(){
        val intent = Intent()
        intent.setClass(this, SesionActivity::class.java)
        startActivityForResult(intent,1)
    }



}
