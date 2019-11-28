package com.example.piscisoftmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

import android.view.View
import android.widget.Toast
import com.example.piscisoftmobile.Model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_detalle_reserva.*
import java.lang.Exception
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DetalleReservaActivity : AppCompatActivity() , OnDataFinishedListener {

    val usuarioFirebase = UsuarioFirebase()
    val reservaFirebase = ReservaFirebase()
    lateinit var reserva : Reserva
    lateinit var turno : Turno
    lateinit var profesor : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_reserva)

        reserva = intent.getSerializableExtra("reserva") as Reserva
        turno = intent.getSerializableExtra("turno") as  Turno
        profesor = intent.getStringExtra("profesor")
        usuarioFirebase.obtenerNombreUsuarioById(this,reserva.codUsuario!!)

    }

    override fun OnUserNombreDataFinished(nombreUsuario : String) {
        nombre.text = nombre.text.toString() + nombreUsuario
        codigo.text = codigo.text.toString() + reserva.codUsuario
        horario.text = horario.text.toString() + turno.horaInicio + " - " + turno.horaFin
        fechaT.text = fechaT.text.toString() + fechaParse(turno.fecha.toString())
        fechaR.text = fechaR.text.toString() + fechaParse(reserva.fechaReserva.toString())
        profesorTV.text = profesor
        modalidad.text = modalidad.text.toString() + reserva.modalidad

        if (reserva.estado == "Pendiente"){
            generarQR(reserva.codReserva!!)
            btnCancelar.setOnClickListener { cancelarReserva(reserva) }
        }

        else if (reserva.estado == "Justificada"){
            btnCancelar.visibility = View.INVISIBLE
            tv_justificacion.text = "Observaciones de la justificación: Justificación enviada"

        }

        else if (reserva.estado == "Cancelada" || reserva.estado == "Inasistida"){
            btnCancelar.visibility = View.INVISIBLE
            buscarJustificacion(reserva.codReserva!!)
        }

    }

    fun buscarJustificacion(codReserva:String) {
        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("justificacion").whereEqualTo("codReserva",codReserva)
        ref.get().addOnSuccessListener {documents ->
            for (document in documents){
                val justificacion = document.toObject(Justificacion::class.java)
                tv_justificacion.text = "Observaciones de la justificación: ${justificacion.observaciones}"
            }
        }

    }

    fun fechaParse(string : String) : String{
        var date = LocalDate.parse(string)
        return "${date.dayOfMonth}/${date.monthValue}/${date.year}"
    }

    fun generarQR(codigo: String){
        try{
            val barcodeEncoder = BarcodeEncoder()
            val bitMap = barcodeEncoder.encodeBitmap(codigo, BarcodeFormat.QR_CODE,400,400)
            imageView_QR.setImageBitmap(bitMap)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun cancelarReserva(reserva: Reserva){

        val hoy = LocalDate.now()
        val formato = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val fechaTurno = LocalDate.parse(turno.fecha, formato)


        if (fechaTurno.isEqual(hoy)){
            irAJustificarActivity()
        }

        else {
            reservaFirebase.cancelarReserva(false,reserva)
            Toast.makeText(this, "¡Reserva cancelada!", Toast.LENGTH_SHORT).show()
            Handler().postDelayed(
                {
                    irAPerfil()
                },
                1000 // value in milliseconds
            )
        }
    }
    
    fun irAPerfil() {
        val intent = Intent()
        intent.setClass(this, SesionActivity::class.java)
        startActivityForResult(intent,1)
    }

    fun irAJustificarActivity(){
        val intent = Intent()
        intent.setClass(this, JustificarActivity::class.java)
        intent.putExtra("reserva",reserva)
        intent.putExtra("turno",turno)
        startActivityForResult(intent,1)
    }
}
