package com.example.piscisoftmobile

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.piscisoftmobile.Model.Horario
import com.example.piscisoftmobile.Model.Profesor
import com.example.piscisoftmobile.Model.Reserva
import com.example.piscisoftmobile.Model.Turno
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_reserva.view.*
import java.time.LocalDate

class ReservasRecyclerAdapter: RecyclerView.Adapter<ReservasRecyclerAdapter.ViewHolder> {
    private var mContext: Context
    private var listaReservas : List<Reserva>
    private var codigoUsuario: String
    val intent = Intent()

    val db = FirebaseFirestore.getInstance()

    constructor(mContext: Context, listaReservas: List<Reserva>, codigoUsuario: String) : super() {
        this.mContext = mContext
        this.listaReservas = listaReservas
        this.codigoUsuario = codigoUsuario
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(mContext).inflate(R.layout.item_reserva,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaReservas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reserva: Reserva = listaReservas.get(position)

        if (reserva.estado == "Pendiente"){
            holder.item_estado.text = "Pendiente"
            holder.item_estado.setTextColor(Color.BLUE)
        }else{
            holder.item_estado.text = "Asistió"
        }

        db.collection("turno").document(reserva.codTurno.toString())
            .get().addOnSuccessListener { document ->
                val turno = document.toObject(Turno::class.java)

                var date = LocalDate.parse(turno!!.fecha.toString())
                var fechaF = "Fecha: ${date.dayOfMonth}/${date.monthValue}/${date.year}"
                holder.item_fecha.text = fechaF
                holder.item_hora.text = turno!!.horaInicio + " - " + turno!!.horaFin
                colocarProfesor(holder,position,turno.profesor.toString())

            }

        //Ver reserva

        var date = LocalDate.parse(reserva.fechaReserva!!.toString())
        var fechaReserva = "${date.dayOfMonth}/${date.monthValue}/${date.year}"
        holder.item_holder.setOnClickListener {
            irDetalleReserva(fechaReserva, holder.item_fecha.text.toString(),
                    holder.item_hora.text.toString(),
                holder.item_profesor.text.toString(),
                reserva.modalidad.toString(),
                reserva.estado.toString()) }
    }

    fun irDetalleReserva(fechaReserva: String, fechaTurno: String, hora: String, profesor: String, modalidad: String, estado: String) {
        val intent = Intent()
        intent.putExtra("fechaR",fechaReserva)
        intent.putExtra("fechaT",fechaTurno)
        intent.putExtra("hora",hora)
        intent.putExtra("profesor",profesor)
        intent.putExtra("modalidad",modalidad)
        intent.putExtra("estado", estado)
        intent.putExtra("codigo",this.codigoUsuario)
        intent.setClass(mContext, DetalleReservaActivity::class.java)
        mContext.startActivity(intent)
    }

    fun colocarProfesor (holder: ViewHolder, position: Int, codProfesor:String){
        db.collection("profesor").document(codProfesor)
            .get().addOnSuccessListener { document ->
                val profesor = document.toObject(Profesor::class.java)
                holder.item_profesor.text = "Profesor(a): "+profesor!!.nombre
            }
    }

    class ViewHolder
    constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item_estado = itemView.item_estado
        val item_fecha= itemView.fecha_reserva
        val item_profesor = itemView.profesor_asignado
        val item_hora = itemView.item_hora_reserva
        val item_holder = itemView.item_holder_historial
    }
}