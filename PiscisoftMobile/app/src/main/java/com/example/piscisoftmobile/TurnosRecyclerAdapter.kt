package com.example.piscisoftmobile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.example.piscisoftmobile.Model.Horario
import com.example.piscisoftmobile.Model.Profesor
import com.example.piscisoftmobile.Model.Turno
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_turno.*
import kotlinx.android.synthetic.main.item_turno.view.*

class TurnosRecyclerAdapter : RecyclerView.Adapter<TurnosRecyclerAdapter.ViewHolder>{

    private lateinit var mContext: Context
    private lateinit var listaTurnos : List<Turno>

    val db = FirebaseFirestore.getInstance()

    constructor(mContext: Context, listaTurnos: List<Turno>) : super() {
        this.mContext = mContext
        this.listaTurnos = listaTurnos
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(mContext).inflate(R.layout.item_turno,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaTurnos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val turno: Turno = listaTurnos.get(position)

        if (turno.abierto == true){
            holder.item_disponibilidad.text = "Disponible"
        }else{
            holder.item_disponibilidad.text = "Cerrado"
        }

        db.collection("horario").document(turno.codHorario!!)
            .get().addOnSuccessListener { document ->
                val horario = document.toObject(Horario::class.java)
                holder.item_hora.text = horario!!.horaInicio + " - " + horario!!.horaFin
                holder.item_capacidad.text = "Capacidad: ${turno.capacidadCubierta}/${horario.capacidadTotal}"
                colocarProfesor(holder,position,horario.codProfesor.toString())
            }

        //Confirmar reserva
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
        val item_disponibilidad = itemView.item_disponibilidad
        val item_capacidad = itemView.item_capacidad
        val item_profesor = itemView.item_profesor
        val item_hora = itemView.item_hora
        val item_holder = itemView.item_holder
    }

}