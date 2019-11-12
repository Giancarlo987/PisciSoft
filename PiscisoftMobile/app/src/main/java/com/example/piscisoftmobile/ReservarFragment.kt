package com.example.piscisoftmobile


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import android.widget.Toast
import com.example.piscisoftmobile.Model.Turno
import com.example.piscisoftmobile.Model.TurnoFirebase
import com.example.piscisoftmobile.Model.UsuarioFirebase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_reservar.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.Year
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*

class ReservarFragment : Fragment() , OnDataFinishedListener {

    lateinit var mContext: Context
    lateinit var hoy: LocalDate
    lateinit var fechaEscogida: LocalDate
    lateinit var fecha : String
    lateinit var userID : String

    val turnoFirebase = TurnoFirebase()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_reservar, container, false)
        val calendarView : CalendarView = root.findViewById(R.id.calendario)

        userID = retornarUserID()

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            if (validarFechaVigente(year, month, dayOfMonth)){
                turnoFirebase.existenTurnosRegistrados(this,fecha)
            } else {
                Toast.makeText( context, "Escoja otra fecha por favor", Toast.LENGTH_SHORT).show()
            }
        }

        mContext = root.context
        return root
    }

    fun validarFechaVigente(year:Int, month:Int, dayOfMonth:Int) : Boolean{

        var dia = ""
        var mes = ""

        if (dayOfMonth<10){
            dia = "0${dayOfMonth}"
        }else{
            dia = "${dayOfMonth}"
        }
        if ((month+1)<10){
            mes = "0${(month + 1)}"
        }else{
            mes = "${(month + 1)}"
        }

        fecha = "${year}-${mes}-${dia}"

        this.hoy = LocalDate.now()
        //this.hoy = LocalDate.of(2019,11,1) // Simular que es viernes
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        this.fechaEscogida = LocalDate.parse(fecha, formatter)

        val woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()
        val weekHoy = hoy.get(woy)
        val weekEscogida = fechaEscogida.get(woy)

        if (fechaEscogida.isBefore(hoy) || weekHoy!=weekEscogida){
            if ((hoy.dayOfWeek.toString() == "FRIDAY" || hoy.dayOfWeek.toString() == "SATURDAY") && (weekHoy==(weekEscogida-1))) {
                return true
            }
            return false
        }
        return true
    }

    override fun OnVerificacionFinished(existe: Boolean) {
        if (existe){
            irATurnosActivity()
        } else {
            Toast.makeText( context, "No hay turnos registrados para esa fecha", Toast.LENGTH_SHORT).show()
        }
    }

    private fun irATurnosActivity(){
        val intent = Intent()
        intent.putExtra("fecha",fecha)
        intent.putExtra("userID",userID)
        intent.setClass(mContext, TurnosActivity::class.java)
        startActivity(intent)
    }

    private fun retornarUserID():String {
        val sharedPreferences : SharedPreferences = requireActivity().getSharedPreferences("login",
            Context.MODE_PRIVATE)
        var userID = sharedPreferences.getString("userID","")
        return userID!!
    }

}