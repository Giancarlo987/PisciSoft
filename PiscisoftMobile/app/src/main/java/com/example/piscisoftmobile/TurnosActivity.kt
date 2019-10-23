package com.example.piscisoftmobile

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.piscisoftmobile.Model.Turno
import com.example.piscisoftmobile.Model.TurnoFirebase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_turnos.*

class TurnosActivity : AppCompatActivity(), OnDataFinishedListener {

    val turnoFirebase = TurnoFirebase()

    lateinit var fecha : String
    lateinit var userID : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turnos)

        fecha = intent.getStringExtra("fecha")
        userID = intent.getStringExtra("userID")
        turnoFirebase.actualizarTurnos(this, fecha)
    }

    override fun OnActualizacionFinished() {
        turnoFirebase.obtenerTurnosByFecha(this, fecha)
    }

    override fun OnListaTurnosDataFinished(listaTurnos : List<Turno>) {
        setRecyclerAdapter(listaTurnos)
    }

    fun setRecyclerAdapter(listaTurnos:List<Turno>){
        val recyclerView: RecyclerView = turnos_recycler_view
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = TurnosRecyclerAdapter(this, listaTurnos, userID)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

}

