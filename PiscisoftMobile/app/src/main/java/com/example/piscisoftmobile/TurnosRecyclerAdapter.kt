package com.example.piscisoftmobile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.example.piscisoftmobile.Model.Turno
import kotlinx.android.synthetic.main.item_turno.*
import kotlinx.android.synthetic.main.item_turno.view.*

class TurnosRecyclerAdapter : RecyclerView.Adapter<TurnosRecyclerAdapter.ViewHolder>{

    private lateinit var mContext: Context
    private lateinit var listaTurnos : List<Turno>

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
        holder.item_disponibilidad.text = "${turno.abierto}"
        holder.item_capacidad.text = "${turno.capacidadCubierta}"
        holder.item_profesor.text = "Andrea"
        holder.item_hora.text = "${turno.fecha}"
        holder.item_holder.setOnClickListener(View.OnClickListener { Toast.makeText(mContext, "hola mundo xd",Toast.LENGTH_LONG).show() })
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