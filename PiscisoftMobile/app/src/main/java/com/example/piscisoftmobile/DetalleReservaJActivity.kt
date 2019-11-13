package com.example.piscisoftmobile

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.piscisoftmobile.Model.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detalle_reserva.*
import kotlinx.android.synthetic.main.activity_detalle_reserva_j.*
import java.time.LocalDate

class DetalleReservaJActivity : AppCompatActivity(), OnDataFinishedListener {

    val usuarioFirebase = UsuarioFirebase()
    val reservaFirebase = ReservaFirebase()
    lateinit var reserva : Reserva
    lateinit var turno : Turno
    lateinit var profesor : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_reserva_j)

        reserva = intent.getSerializableExtra("reserva") as Reserva
        turno = intent.getSerializableExtra("turno") as Turno
        profesor = intent.getStringExtra("profesor")
        usuarioFirebase.obtenerNombreUsuarioById(this,reserva.codUsuario!!)
    }

    override fun OnUserNombreDataFinished(nombreUsuario : String) {
        nombre2.text = nombre2.text.toString() + nombreUsuario
        codigo2.text = codigo2.text.toString() + reserva.codUsuario
        horario2.text = horario2.text.toString() + turno.horaInicio + " - " + turno.horaFin
        fechaT2.text = fechaT2.text.toString() + fechaParse(turno.fecha.toString())
        fechaR2.text = fechaR2.text.toString() + fechaParse(reserva.fechaReserva.toString())
        profesorTV2.text = profesor
        modalidad2.text = modalidad2.text.toString() + reserva.modalidad


        if (reserva.estado == "Justificada"){
            estadoJustificacion.text = "Estado: Justificación enviada"
        }

        else if (reserva.estado == "Cancelada" || reserva.estado == "Inasistida"){
            buscarJustificacion(reserva.codReserva!!)
        }
    }

    fun buscarJustificacion(codReserva:String) {
        li.setBackgroundColor(Color.parseColor("#31000000"))
        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("justificacion").whereEqualTo("codReserva",codReserva)
        ref.get().addOnSuccessListener {documents ->
            for (document in documents){
                val justificacion = document.toObject(Justificacion::class.java)
                estadoJustificacion.text = "Estado: "+justificacion.estado
                if (justificacion.observaciones != ""){
                    comentarios.text = "Comentarios: ${justificacion.observaciones}"
                }
            }
        }
        if (estadoJustificacion.text == ""){
            estadoJustificacion.text = "Estado: No se envió justificación"
        }

    }

    fun fechaParse(string : String) : String{
        var date = LocalDate.parse(string)
        return "${date.dayOfMonth}/${date.monthValue}/${date.year}"
    }

}
