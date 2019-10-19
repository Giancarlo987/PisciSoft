package com.example.piscisoftmobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.example.piscisoftmobile.Model.Turno
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_ver_reservas_profesor.*
import java.time.LocalDate
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.piscisoftmobile.Model.Reserva
import kotlinx.android.synthetic.main.activity_turnos.*
import java.time.format.DateTimeFormatter



class VerReservasProfesorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_reservas_profesor)
        setSpinner()
    }

    fun setSpinner() {
        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("turno")
        var hoy = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val query = ref.whereEqualTo("fecha", hoy)


        query.get()
            .addOnSuccessListener { documents ->
                var turnos = mutableListOf<String>()
                for (document in documents) {
                    var turno = document.toObject(Turno::class.java)
                    //If vigente
                    turnos.add(turno.horaInicio!! +" - "+ turno.horaFin!!)
                }
                turnos.sortBy { turno -> turno.substring(0, turno?.indexOf(":")!!)!!.toInt()-1;}
                val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, turnos)
                spinner.adapter = arrayAdapter

                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        Toast.makeText(this@VerReservasProfesorActivity, turnos[position].substring(0, turnos[position]?.indexOf(" ")!!)!!, Toast.LENGTH_SHORT).show()
                        retornarReservas(hoy, turnos[position].substring(0, turnos[position]?.indexOf(" ")!!)!!)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // Code to perform some action when nothing is selected
                    }
                }

            }
            .addOnFailureListener { exception ->
                Log.w("ERROR FIREBASE", "Error getting documents: ", exception)
            }
    }

    fun retornarReservas(fecha:String,horaInicio:String){
        val db = FirebaseFirestore.getInstance()
        db.collection("reserva").whereEqualTo("codTurno",fecha+"."+horaInicio)
            .get()
            .addOnSuccessListener { documents ->
                var reservas = mutableListOf<Reserva>()
                for (document in documents){
                    val reserva = document.toObject(Reserva::class.java)
                    reservas.add(reserva)
                }
                this.setRecyclerAdapter(reservas)
            }
            .addOnFailureListener{ exception ->
                Log.d("ERROR EN FIREBASE", "get failed with ", exception)
            }
    }

    fun setRecyclerAdapter(reservas:List<Reserva>){
        val recyclerView: RecyclerView = reservasProfesor_recycler_view
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ReservasProfesorRecyclerAdapter(this, reservas)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}
