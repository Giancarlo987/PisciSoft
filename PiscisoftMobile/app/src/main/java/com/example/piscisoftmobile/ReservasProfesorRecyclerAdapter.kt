package com.example.piscisoftmobile

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.piscisoftmobile.Model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_perfil.*
import kotlinx.android.synthetic.main.fragment_ver_reserva_detalle.view.*
import kotlinx.android.synthetic.main.item_turno.view.*
import kotlinx.android.synthetic.main.item_turno.view.item_codigo
import kotlinx.android.synthetic.main.item_turno.view.item_foto
import kotlinx.android.synthetic.main.item_turno.view.item_nombre
import kotlinx.android.synthetic.main.item_usuario.view.*

class ReservasProfesorRecyclerAdapter : RecyclerView.Adapter<ReservasProfesorRecyclerAdapter.ViewHolder>, OnDataFinishedListener{

    private lateinit var mContext: Context
    private lateinit var reservas: List<Reserva>
    private lateinit var userID : String
    val intent = Intent()

    val usuarioFirebase = UsuarioFirebase()
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int)  {
        val reserva: Reserva = reservas.get(position)
        usuarioFirebase.obtenerUsuarioById(this, reserva.codUsuario!!, holder, position, reserva)

    }

    override fun OnUserDataFinished(usuario:Usuario, holder: ViewHolder, position: Int, reserva:Reserva) {
        holder.item_codigo.text = "Código: " + usuario!!.codigo
        holder.item_nombre.text = "Nombre: " + usuario!!.nombre
        val url = usuario.foto
        Picasso.get().load(url).fit().into(holder.item_foto)
        holder.item_holder.setOnClickListener { mostrarDetalle(usuario,reserva) }
    }

    class ViewHolder
    constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item_nombre = itemView.item_nombre
        val item_codigo = itemView.item_codigo
        val item_holder = itemView.item_holder_usuario
        val item_foto = itemView.item_foto
    }


    fun mostrarDetalle(usuario: Usuario, reserva: Reserva){

        val dialogBuilder = AlertDialog.Builder(mContext)

        val inflator: LayoutInflater = LayoutInflater.from(mContext)
        val v : View = inflator.inflate(R.layout.fragment_ver_reserva_detalle,null)

        val fotousuario = v.findViewById<ImageView>(R.id.fotousuario)
        dialogBuilder.setView(v)

        val url = usuario.foto

        Picasso.get().load(url).resize(450,450).into(fotousuario)

        val username = v.findViewById<TextView>(R.id.username)
        username.text = username.text.toString() + usuario.nombre.toString()

        val usereid = v.findViewById<TextView>(R.id.userid)
        usereid.text = usereid.text.toString() + usuario.codigo.toString()

        val tipousuario = v.findViewById<TextView>(R.id.tipousuario)
        tipousuario.text = tipousuario.text.toString() + usuario.tipo.toString()

        val nivelusuario = v.findViewById<TextView>(R.id.nivelusuario)
        nivelusuario.text = nivelusuario.text.toString() + usuario.nivel.toString()

        val observacionesusuario = v.findViewById<TextView>(R.id.observacionesusuario)
        observacionesusuario.text = observacionesusuario.text.toString() + usuario.observaciones.toString()

        val modalidadusuario = v.findViewById<TextView>(R.id.modalidadusuario)
        modalidadusuario.text = modalidadusuario.text.toString() + reserva.modalidad.toString()

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        //alert.setTitle(usuario.codigo)
        // show alert dialog
        v.findViewById<Button>(R.id.ok).setOnClickListener {  alert.dismiss()}
        alert.show()
    }

}