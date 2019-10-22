package com.example.piscisoftmobile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.piscisoftmobile.Model.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_confirmar_reserva.*
import kotlinx.android.synthetic.main.activity_detalle_reserva.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ConfirmarReservaActivity : AppCompatActivity(), OnDataFinishedListener {

    private lateinit var codTurno : String
    private lateinit var codUsuario : String
    private lateinit var modalidad : String
    val usuarioFirebase = UsuarioFirebase()
    val reservaFirebase = ReservaFirebase()

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
        tv_profesor.text = profesor

        btn_confirmar.setOnClickListener { validarCampos() }
        btn_cancelar.setOnClickListener { irPerfil() }

    }


    fun validarCampos() {
        if (!rb_1.isChecked && !rb_2.isChecked){
            Toast.makeText(this, "Por favor, ingrese la modalidad", Toast.LENGTH_SHORT).show()
        } else {
            if (rb_1.isChecked){
                modalidad = "Aprendizaje y mejora"
            }else {
                modalidad = "Práctica libre"
            }
            usuarioFirebase.verificarUsuarioHabilitado(this,codUsuario)
        }
    }

    override fun OnUsuarioHabilitadoFinished(habilitado : Boolean) {
        if (habilitado){
            reservaFirebase.existeReservaEsteDia(this,codUsuario,codTurno)
        } else {
            Toast.makeText(this, "No puede realizar reservas, se encuentra suspendido", Toast.LENGTH_SHORT).show()
        }
    }

    override fun OnVerificacionFinished(existe : Boolean) {
        if (existe){
            Toast.makeText(this, "Ya cuenta con reservas para este día", Toast.LENGTH_SHORT).show()
        } else {
            var reserva = Reserva()
            reserva.codTurno = this.codTurno
            reserva.codUsuario = this.codUsuario
            reserva.modalidad = modalidad
            val formatter = DateTimeFormatter. ofPattern("yyyy-MM-dd")
            val current = LocalDateTime.now()
            val formatted = current. format(formatter)
            reserva.fechaReserva = formatted
            reserva.estado = "Pendiente"

            reservaFirebase.registrarReserva(this,reserva)
        }
    }

    override fun OnRegistroReservaFinished() {
        Toast.makeText(this, "¡Reserva realizada!", Toast.LENGTH_SHORT).show()
        irPerfil()
    }

    private fun irPerfil(){
        val intent = Intent()
        intent.setClass(this, SesionActivity::class.java)
        startActivityForResult(intent,1)
    }



}
