package com.example.piscisoftmobile.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.piscisoftmobile.R

class ModificarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modificar)
        actionBar?.setDisplayHomeAsUpEnabled(true)

    }

}
