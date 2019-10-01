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

class TurnosActivity : AppCompatActivity() {

    val turnoFirebase = TurnoFirebase()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turnos)

        val fecha = intent.getStringExtra("fecha")
        val userID = intent.getStringExtra("userID")
        Toast.makeText( this, fecha + " " + userID, Toast.LENGTH_SHORT).show()

        turnoFirebase.retornarTurnos(this, fecha)
    }

    fun setRecyclerAdapter(listaTurnos:List<Turno>){
        val recyclerView: RecyclerView = turnos_recycler_view
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = TurnosRecyclerAdapter(this, listaTurnos)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }



}

