package com.example.piscisoftmobile

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.piscisoftmobile.Model.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_reserva.view.*
import java.time.LocalDate

class ReservasRecyclerAdapter: RecyclerView.Adapter<ReservasRecyclerAdapter.ViewHolder> , OnDataFinishedListener {
    private var mContext: Context
    private var listaReservas : List<Reserva>
    private var codigoUsuario: String
    val intent = Intent()

    val turnoFirebase = TurnoFirebase()
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val reserva: Reserva = listaReservas.get(position)
        if (reserva.estado == "Pendiente"){
            holder.item_estado.text = "Pendiente"
            holder.item_image.setImageResource(R.drawable.pendiente)
            holder.item_estado.setTextColor(Color.BLUE)
        }
        else if (reserva.estado == "Asistido"){
            holder.item_estado.text = "Asistida"
            holder.item_image.setImageResource(R.drawable.asistida)
            holder.item_estado.setTextColor(Color.rgb(255,165,0))
        }
        else if (reserva.estado == "Inasistida"){
            holder.item_estado.text = "Inasistida"
            holder.item_image.setImageResource(R.drawable.inasistida)
            holder.item_estado.setTextColor(Color.GRAY)
        }
        else if (reserva.estado == "Justificada"){
            holder.item_estado.text = "Justificada"
            holder.item_image.setImageResource(R.drawable.justificada)
            holder.item_estado.setTextColor(Color.MAGENTA)
        }
        else if (reserva.estado == "Cancelada"){
            holder.item_estado.text = "Cancelada"
            holder.item_image.setImageResource(R.drawable.reservacancelada)
            holder.item_estado.setTextColor(Color.BLACK)
        }
        turnoFirebase.obtenerTurnoByCodigo(this, reserva.codTurno!!, holder, position, reserva)
    }


    override fun OnTurnoDataFinished(turno:Turno, holder: ViewHolder, position: Int, reserva:Reserva) {
        var date = LocalDate.parse(turno!!.fecha.toString())
        var fechaF = "Fecha: ${date.dayOfMonth}/${date.monthValue}/${date.year}"
        holder.item_fecha.text = fechaF
        holder.item_hora.text = turno!!.horaInicio + " - " + turno!!.horaFin
        colocarProfesor(holder,position,turno.profesor.toString())
        if (reserva.estado == "Pendiente"){
            holder.item_holder.setOnClickListener{irDetalleReservaActivity(reserva,holder.item_profesor.text.toString(),turno)}
        } else {
            holder.item_holder.setOnClickListener{irDetalleReservaJActivity(reserva,holder.item_profesor.text.toString(),turno)}
        }
    }

    fun irDetalleReservaActivity(reserva:Reserva,profesor:String,turno:Turno){
        val intent = Intent()
        intent.putExtra("reserva",reserva)
        intent.putExtra("profesor",profesor)
        intent.putExtra("turno",turno)
        intent.setClass(mContext, DetalleReservaActivity::class.java)
        mContext.startActivity(intent)
    }

    fun irDetalleReservaJActivity(reserva:Reserva,profesor:String,turno:Turno){
        val intent = Intent()
        intent.putExtra("reserva",reserva)
        intent.putExtra("profesor",profesor)
        intent.putExtra("turno",turno)
        intent.setClass(mContext, DetalleReservaJActivity::class.java)
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
        val item_image = itemView.item_foto
    }
}