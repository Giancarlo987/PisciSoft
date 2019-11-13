package com.example.piscisoftmobile

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.piscisoftmobile.Model.Reserva
import com.example.piscisoftmobile.Model.ReservaFirebase
import com.example.piscisoftmobile.Model.Turno
import com.example.piscisoftmobile.Model.TurnoFirebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class PerfilProfesorActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    val refReserva = db.collection("reserva")
    val refTurno= db.collection("turno")
    val reservaFirebase = ReservaFirebase()
    var hoy = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_profesor)

        var but_reservas = findViewById<Button>(R.id.btn_ver_reservas)
        but_reservas.setOnClickListener { irAVerReservasProfesorActivity() }

        var but_aisitencias = findViewById<Button>(R.id.btn_colocar_asistencia)
        but_aisitencias.setOnClickListener { IniciarScanner() }

    }

    private fun irAVerReservasProfesorActivity(){
        val intent = Intent()
        intent.setClass(this, VerReservasProfesorActivity::class.java)
        startActivityForResult(intent,1)
    }

    private fun IniciarScanner(){
        val scanner = IntentIntegrator(this)
        scanner.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK){
            val result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
            if(result != null){
                if(result.contents == null){
                    Toast.makeText(this,"Cancelado", Toast.LENGTH_LONG).show()
                }else{
                    ColoarAsistencia(result.contents)
                }
            }else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    fun guardarAsistencia(reserva: Reserva, codigo: String){

        val query = refTurno.document(reserva.codTurno!!)
        query.get()
            .addOnSuccessListener { document ->
                val turno = document.toObject(Turno::class.java)
                Toast.makeText(this,"hola", Toast.LENGTH_LONG).show()
                if (turno == null){
                    Toast.makeText(this,"Turno expirado", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this,"Turno: " +turno.fecha, Toast.LENGTH_LONG).show()

                    if (turno.fecha == hoy){
                        reserva.estado = "Asistido"
                        val db = FirebaseFirestore.getInstance()
                        db.collection("reserva").document(codigo).set(reserva)
                        Toast.makeText(this, "Asistencia registrada", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this,"La reserva no es correcta", Toast.LENGTH_LONG).show()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this,"No turno", Toast.LENGTH_LONG).show()
            }
    }

    private fun ColoarAsistencia(codigo: String){

        val query = refReserva.document(codigo)
        query.get()
            .addOnSuccessListener { document ->
                val reserva = document.toObject(Reserva::class.java)
                if (reserva == null){
                    Toast.makeText(this,"Reserva expirada", Toast.LENGTH_LONG).show()
                } else {
                    guardarAsistencia(reserva,codigo)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this,"No reserva", Toast.LENGTH_LONG).show()
            }
    }

    /*override fun OnListaReservaDataFinished(reserva: Reserva){
        if (reserva.fechaReserva == hoy){
            reserva.estado == "Asistido"
            val db = FirebaseFirestore.getInstance()
            db.collection("reserva").document(reserva.codReserva!!).set(reserva)
            Toast.makeText(this, "Asistencia registrada", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"La reserva ya caducó", Toast.LENGTH_LONG).show()
        }
    }*/


}
