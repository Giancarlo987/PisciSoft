package com.example.piscisoftmobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.widget.Toast
import com.example.piscisoftmobile.Model.*
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_detalle_reserva.*
import java.lang.Exception
import java.time.LocalDate

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
        }else{
            btnCancelar.visibility = View.INVISIBLE
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
        // Si no es hoy se elimina
        reservaFirebase.eliminarReserva(reserva)
        Toast.makeText(this, "Cancelar la reserva", Toast.LENGTH_SHORT).show()
    }
}
