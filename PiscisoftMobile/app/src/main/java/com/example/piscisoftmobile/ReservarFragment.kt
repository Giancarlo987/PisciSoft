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
import java.time.Month
import java.time.Year

val usuarioFirebase = UsuarioFirebase()
val turnoFirebase = TurnoFirebase()
var userID = ""

class ReservarFragment : Fragment() {

    var fecha = ""
    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_reservar, container, false)
        val calendarView : CalendarView = root.findViewById(R.id.calendario)

        //val btn_prueba: Button = root.findViewById(R.id.btn_prueba)
        //val btn_modificar: Button = root.findViewById(R.id.btn_modificar)

        userID = retornarUserID()


        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            fecha = "${year}-${(month + 1)}-${dayOfMonth}"
            Toast.makeText( context, fecha, Toast.LENGTH_SHORT).show()
            turnoFirebase.existenTurnos(this,fecha)
        }

        mContext = root.context

        return root
    }

    fun irTurnos(existen:Boolean){ //Dirigirse a ver los turnos
        if (existen){
            Toast.makeText( context, "Si hay turnos", Toast.LENGTH_SHORT).show()
            val intent = Intent()
            intent.putExtra("fecha",fecha)
            intent.putExtra("userID",userID)
            intent.setClass(mContext, TurnosActivity::class.java)
            startActivity(intent)

        } else {
            Toast.makeText( context, "No hay turnos registrados para esa fecha", Toast.LENGTH_SHORT).show()
        }
    }

    private fun retornarUserID():String {
        val sharedPreferences : SharedPreferences = requireActivity().getSharedPreferences("login",
            Context.MODE_PRIVATE)
        var userID = sharedPreferences.getString("userID","")
        //Toast.makeText( context, userID, Toast.LENGTH_SHORT).show()
        return userID!!
    }


}