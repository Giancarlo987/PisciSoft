package com.example.piscisoftmobile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.widget.Toast

class ReservarFragment : Fragment() {

    private lateinit var dashboardViewModel: ReservarViewModel
    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(ReservarViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_reservar, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(this, Observer {
            textView.text = it
        })

        //val btn_modificar: Button = root.findViewById(R.id.btn_modificar)



        val calendarView : CalendarView = root.findViewById(R.id.calendario)
        calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            val msg = "Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year

            Toast.makeText( context, msg, Toast.LENGTH_SHORT).show()
        }

        return root
    }
}