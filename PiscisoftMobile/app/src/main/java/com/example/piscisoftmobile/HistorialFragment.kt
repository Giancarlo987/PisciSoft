package com.example.piscisoftmobile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.piscisoftmobile.Model.Reserva
import com.example.piscisoftmobile.Model.ReservaFirebase
import com.example.piscisoftmobile.Model.UsuarioFirebase
import kotlinx.android.synthetic.main.fragment_historial.*

val userFirebase = UsuarioFirebase()
val reservaFirebase = ReservaFirebase()
var codigoUsuario = ""

class HistorialFragment : Fragment() {

    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_historial, container, false)

        codigoUsuario = retornarUserID()

        reservaFirebase.existenReservas(this,codigoUsuario)

        mContext = root.context

        return root
    }

    fun irReservas(existen:Boolean){ //Dirigirse a ver los turnos
        if (existen){
            Toast.makeText( context, "Si hay reservas", Toast.LENGTH_SHORT).show()
            reservaFirebase.retornarReservas(this, codigoUsuario)
        } else {
            Toast.makeText( context, "No hay reservas realizadas", Toast.LENGTH_SHORT).show()
        }
    }

    fun setRecyclerAdapter(listaReservas:List<Reserva>){
        val recyclerView: RecyclerView = reservas_recycler_view
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        val adapter = ReservasRecyclerAdapter(mContext, listaReservas, codigoUsuario)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun retornarUserID():String {
        val sharedPreferences : SharedPreferences = requireActivity().getSharedPreferences("login",
            Context.MODE_PRIVATE)
        var userID = sharedPreferences.getString("userID","")
        return userID!!
    }

}