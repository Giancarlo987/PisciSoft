package com.example.piscisoftmobile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.piscisoftmobile.Model.ReservaFirebase

class ReservacionActivity : AppCompatActivity() {
    val reservaFirebase = ReservaFirebase()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turnos)

        val fecha = intent.getStringExtra("fecha")
        val userID = intent.getStringExtra("userID")
        Toast.makeText( this, fecha + " " + userID, Toast.LENGTH_SHORT).show()

        turnoFirebase.retornarTurnos(this, fecha)
    }

    fun setRecyclerAdapter(listaTurnos:List<Turno>){
        val recyclerView: RecyclerView = null
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = TurnosRecyclerAdapter(this, listaTurnos, userID)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

}