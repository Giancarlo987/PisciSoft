package com.example.piscisoftmobile

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.piscisoftmobile.Model.Profesor
import com.example.piscisoftmobile.Model.Turno
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_turno.view.*

class TurnosRecyclerAdapter : RecyclerView.Adapter<TurnosRecyclerAdapter.ViewHolder>{

    private lateinit var mContext: Context
    private lateinit var listaTurnos : List<Turno>
    private lateinit var userID : String
    val intent = Intent()

    val db = FirebaseFirestore.getInstance()

    constructor(mContext: Context, listaTurnos: List<Turno>, userID:String) : super() {
        this.mContext = mContext
        this.listaTurnos = listaTurnos
        this.userID = userID
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(mContext).inflate(R.layout.item_turno, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaTurnos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val turno: Turno = listaTurnos.get(position)

        holder.item_disponibilidad.text = turno.estado

        if (turno.estado == "Abierto"){
            holder.item_disponibilidad.setTextColor(Color.GREEN)
            holder.item_image.setImageResource(R.drawable.disponible)
            holder.item_capacidad.text = "Capacidad: ${turno.capacidadCubierta}/${turno.capacidadTotal}"
            intent.putExtra("capacidadTotal",turno.capacidadTotal)
            holder.item_holder.setOnClickListener { irConfirmarReserva(turno.id!!,turno.fecha!!,holder.item_hora.text.toString(),holder.item_profesor.text.toString() ) }

        } else if (turno.estado == "Cerrado") {
            holder.item_disponibilidad.setTextColor(Color.RED)
            holder.item_image.setImageResource(R.drawable.cancelada)
            holder.item_capacidad.text = "Observaciones: ${turno.observaciones}"
            holder.item_holder.setOnClickListener { Toast.makeText(mContext, "Este turno se encuentra cerrado", Toast.LENGTH_SHORT).show() }
        } else {
            holder.item_disponibilidad.setTextColor(Color.BLACK)
            holder.item_image.setImageResource(R.drawable.cancelada)
            holder.item_capacidad.text = "Capacidad: ${turno.capacidadCubierta}/${turno.capacidadTotal}"
            holder.item_holder.setOnClickListener { Toast.makeText(mContext, "Este turno ya pasó", Toast.LENGTH_SHORT).show() }
        }

        holder.item_hora.text = turno!!.horaInicio + " - " + turno!!.horaFin
        colocarProfesor(holder,position,turno.profesor.toString())

    }


    fun colocarProfesor (holder: ViewHolder, position: Int, codProfesor:String){
        db.collection("profesor").document(codProfesor)
            .get().addOnSuccessListener { document ->
                val profesor = document.toObject(Profesor::class.java)
                holder.item_profesor.text = "Profesor(a): " + profesor!!.nombre
                }
    }

    private fun irConfirmarReserva(codTurno:String,fecha:String,hora:String,nombreProfesor:String ){
        intent.putExtra("codTurno",codTurno)
        intent.putExtra("fecha",fecha)
        intent.putExtra("horario",hora)
        intent.putExtra("profesor",nombreProfesor)
        intent.putExtra("userID",this.userID)
        intent.setClass(mContext, ConfirmarReservaActivity::class.java)
        mContext.startActivity(intent)
    }

    class ViewHolder
    constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item_disponibilidad = itemView.item_nombre
        val item_capacidad = itemView.item_codigo
        val item_profesor = itemView.item_profesor
        val item_hora = itemView.item_hora
        val item_holder = itemView.item_holder
        val item_image = itemView.item_foto
    }

}