package com.example.piscisoftmobile

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class TurnosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turnos)

        val fecha = intent.getStringExtra("fecha")
        val userID = intent.getStringExtra("userID")
        Toast.makeText( this, fecha + " " + userID, Toast.LENGTH_SHORT).show()
    }

}

