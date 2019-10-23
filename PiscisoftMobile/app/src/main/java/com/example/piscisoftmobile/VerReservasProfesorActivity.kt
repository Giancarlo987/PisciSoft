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
import com.example.piscisoftmobile.Model.ReservaFirebase
import com.example.piscisoftmobile.Model.TurnoFirebase
import kotlinx.android.synthetic.main.activity_turnos.*
import java.time.format.DateTimeFormatter



class VerReservasProfesorActivity : AppCompatActivity() , OnDataFinishedListener {

    val turnoFirebase = TurnoFirebase()
    val reservaFirebase = ReservaFirebase()
    var horarios = mutableListOf<String>()
    var hoy = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_reservas_profesor)
        turnoFirebase.obtenerTurnosByFecha(this, hoy)

    }

    override fun OnListaTurnosDataFinished(listaTurnos : List<Turno>) {
        horarios = mutableListOf<String>()
        for (turno in listaTurnos){
            horarios.add(turno.horaInicio!! +" - "+ turno.horaFin!!)
        }
        setSpinner(horarios)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@VerReservasProfesorActivity, horarios[position].substring(0, horarios[position]?.indexOf(" ")!!)!!, Toast.LENGTH_SHORT).show()
                var codTurno = hoy + "." + horarios[position].substring(0, horarios[position]?.indexOf(" ")!!)!!
                reservaFirebase.obtenerReservasEnTurno(this@VerReservasProfesorActivity, codTurno)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    fun setSpinner(horarios:List<String>){
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, horarios)
        spinner.adapter = arrayAdapter

        //RICARDO Agregar más espacio entre los horarios que aparecen en el spinner:
        // https://android--code.blogspot.com/2015/08/android-spinner-text-padding.html

    }

    override fun OnListaReservasDataFinished(listaReservas : List<Reserva>) {
        if (listaReservas.isEmpty()){
            setRecyclerAdapter(listaReservas)
            Toast.makeText(this@VerReservasProfesorActivity, "No hay reservas registradas en este turno", Toast.LENGTH_SHORT).show()

        } else {
            setRecyclerAdapter(listaReservas)
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
