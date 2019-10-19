package com.example.piscisoftmobile

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.piscisoftmobile.Model.Profesor
import com.example.piscisoftmobile.Model.Reserva
import com.example.piscisoftmobile.Model.Turno
import com.example.piscisoftmobile.Model.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_perfil.*
import kotlinx.android.synthetic.main.item_turno.view.*
import kotlinx.android.synthetic.main.item_turno.view.item_codigo
import kotlinx.android.synthetic.main.item_turno.view.item_foto
import kotlinx.android.synthetic.main.item_turno.view.item_nombre
import kotlinx.android.synthetic.main.item_usuario.view.*

class ReservasProfesorRecyclerAdapter : RecyclerView.Adapter<ReservasProfesorRecyclerAdapter.ViewHolder>{

    private lateinit var mContext: Context
    private lateinit var reservas: List<Reserva>
    private lateinit var userID : String
    val intent = Intent()

    val db = FirebaseFirestore.getInstance()

    constructor(mContext: Context, reservas: List<Reserva>) : super() {
        this.mContext = mContext
        this.reservas = reservas
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(mContext).inflate(R.layout.item_usuario, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reservas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reserva: Reserva = reservas.get(position)


        db.collection("usuario").document(reserva.codUsuario!!).get()
            .addOnSuccessListener { document ->
                val usuario = document.toObject(Usuario::class.java)
                holder.item_codigo.text = "Código: " + usuario!!.codigo
                holder.item_nombre.text = "Nombre: " + usuario!!.nombre
                val url = usuario.foto
                Picasso.get().load(url).fit().into(holder.item_foto)
                holder.item_holder.setOnClickListener { mostrarDetalle(usuario,reserva) }
            }

    }



    class ViewHolder
    constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item_nombre = itemView.item_nombre
        val item_codigo = itemView.item_codigo
        val item_holder = itemView.item_holder_usuario
        val item_foto = itemView.item_foto
    }

    fun mostrarDetalle(usuario:Usuario,reserva:Reserva){

        val dialogBuilder = AlertDialog.Builder(mContext)

        // set message of alert dialog
        dialogBuilder.setMessage(
                "\nNombre: " + usuario.nombre +
                "\nCódigo: " + usuario.codigo +
                "\nTipo de usuario: " + usuario.tipo +
                "\nNivel de natación: " + usuario.nivel +
                "\nObservaciones: " + usuario.observaciones +
                "\nModalidad: " + reserva.modalidad +
                "")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Proceed", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })
            // negative button text and action
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle(usuario.codigo)
        // show alert dialog
        alert.show()
    }

}