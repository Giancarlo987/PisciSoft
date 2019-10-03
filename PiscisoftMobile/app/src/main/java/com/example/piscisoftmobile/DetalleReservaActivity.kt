package com.example.piscisoftmobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.piscisoftmobile.Model.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_detalle_reserva.*
import java.lang.Exception

class DetalleReservaActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    val ref = db.collection("usuario")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_reserva)

        val codigoUsuario = intent.getStringExtra("codigo")
        val query = ref.whereEqualTo("codigo",codigoUsuario)
        query.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val usuario = document.toObject(Usuario::class.java)
                    nombre.setText(nombre.text.toString()+usuario.nombre.toString())
                }
            }
            .addOnFailureListener{ exception ->
                Log.d("ERROR EN FIREBASE", "get failed with ", exception)
            }

        var fecha = findViewById<TextView>(R.id.fecha)
        var hora = findViewById<TextView>(R.id.hora)
        var profesor = findViewById<TextView>(R.id.profesor)
        var modalidad = findViewById<TextView>(R.id.modalidad)
        var codigo = findViewById<TextView>(R.id.codigo)

        codigo.setText(codigo.text.toString()+codigoUsuario)
        fecha.setText(fecha.text.toString() + intent.getStringExtra("fecha"))
        hora.setText(hora.text.toString() + intent.getStringExtra("hora"))
        profesor.setText(profesor.text.toString() + intent.getStringExtra("profesor"))
        modalidad.setText(modalidad.text.toString() + intent.getStringExtra("modalidad"))

        generarQR(codigoUsuario)

    }

    fun generarQR(codigo: String){
        val cod = codigo

        try{
            val barcodeEncoder = BarcodeEncoder()
            val bitMap = barcodeEncoder.encodeBitmap(cod, BarcodeFormat.QR_CODE,400,400)
            imageView_QR.setImageBitmap(bitMap)
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

}
