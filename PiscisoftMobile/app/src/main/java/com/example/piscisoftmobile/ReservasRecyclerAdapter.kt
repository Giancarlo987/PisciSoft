package com.example.piscisoftmobile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.item_turno.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.piscisoftmobile.Model.Horario
import com.example.piscisoftmobile.Model.Profesor
import com.example.piscisoftmobile.Model.Reserva
import com.example.piscisoftmobile.Model.Turno
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_reserva.view.*
import kotlinx.android.synthetic.main.item_turno.view.item_profesor

class ReservasRecyclerAdapter: RecyclerView.Adapter<ReservasRecyclerAdapter.ViewHolder> {
    private lateinit var mContext: Context
    private lateinit var listaReservas : List<Reserva>

    val db = FirebaseFirestore.getInstance()

    constructor(mContext: Context, listaReservas: List<Reserva>) : super() {
        this.mContext = mContext
        this.listaReservas = listaReservas
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
        }else{
            holder.item_estado.text = "Asistió"
        }

        db.collection("turno").document(reserva.codigoTurno.toString())
            .get().addOnSuccessListener { document ->
                val turno = document.toObject(Turno::class.java)
                holder.item_fecha.text = "Fecha: ${turno!!.fecha}"
                db.collection("horario").document(turno.codHorario.toString())
                    .get().addOnSuccessListener { document ->
                        val horario = document.toObject(Horario::class.java)
                        holder.item_hora.text = horario!!.horaInicio + " - " + horario!!.horaFin
                        holder.item_fecha.text = "Capacidad: ${reserva.codigoTurno}"
                        colocarProfesor(holder,position,horario.codProfesor.toString())
                    }
            }

        //Ver reserva
        holder.item_holder.setOnClickListener(View.OnClickListener { Toast.makeText(mContext, "hola mundo xd",Toast.LENGTH_LONG).show() })
    }

    fun colocarProfesor (holder: ViewHolder, position: Int, codProfesor:String){
        db.collection("profesor").document(codProfesor)
            .get().addOnSuccessListener { document ->
                val profesor = document.toObject(Profesor::class.java)
                holder.item_profesor.text = profesor!!.nombre
            }
    }

    class ViewHolder
    constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item_estado = itemView.item_estado
        val item_fecha= itemView.item_fecha
        val item_profesor = itemView.item_profesor_asignado
        val item_hora = itemView.item_hora_reserva
        val item_holder = itemView.item_holder_historial
    }
}