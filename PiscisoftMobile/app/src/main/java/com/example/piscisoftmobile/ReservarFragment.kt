package com.example.piscisoftmobile


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_reservar.*

class ReservarFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_reservar, container, false)
        val calendarView : CalendarView = root.findViewById(R.id.calendario)

        val btn_prueba: Button = root.findViewById(R.id.btn_prueba)


        //val btn_modificar: Button = root.findViewById(R.id.btn_modificar)

        val codigo = savedInstanceState?.get("codigo")
        val sharedPreferences : SharedPreferences = requireActivity().getSharedPreferences("login",
            Context.MODE_PRIVATE)
        var userID = sharedPreferences.getString("userID","")
        Toast.makeText( context, userID, Toast.LENGTH_SHORT).show()

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            val msg = "HOLA Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year
            Toast.makeText( context, msg, Toast.LENGTH_SHORT).show()
        }


        return root
    }


}