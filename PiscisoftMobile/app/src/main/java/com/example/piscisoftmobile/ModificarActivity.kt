package com.example.piscisoftmobile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.piscisoftmobile.Model.Usuario
import com.example.piscisoftmobile.Model.UsuarioFirebase
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_modificar.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ModificarActivity : AppCompatActivity() {

    var niveles = mutableListOf<String>()
    var tipos = mutableListOf<String>()
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null

    private lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modificar)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val but_cancelar = findViewById<Button>(R.id.btn_cancelar_modificar)
        but_cancelar.setOnClickListener { Regresar() }

        val userID = intent.getStringExtra("userID")
        val usuarioFirebase = UsuarioFirebase()
        usuarioFirebase.obtenerUsuarioById(this,userID!!)

        niveles.add("Básico")
        niveles.add("Intermedio")
        niveles.add("Avanzado")

        tipos.add("Alumno")
        tipos.add("Trabajador")
        tipos.add("Egresado")

    }

    fun ActualizarUsuario(usuario: Usuario){
        Toast.makeText(this, "Aun falta", Toast.LENGTH_SHORT).show()

        val db = FirebaseFirestore.getInstance()
        db.collection("usuario").document(usuario.codigo!!).set(usuario)
        Toast.makeText(this, "Modificacion realizada", Toast.LENGTH_SHORT).show()
        Regresar()
    }

    private fun vacio(et: EditText) : Boolean{
        if (et.text.toString().contentEquals("")) {
            return true
        }
        return false
    }

    private fun Subirfoto(usuario: Usuario){

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        storageRef =
            FirebaseStorage.getInstance().reference.child("profiles").child(usuario.codigo.toString() + "_" + timeStamp)

        foto.isDrawingCacheEnabled = true
        foto.buildDrawingCache()

        val bitmap = (foto.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = storageRef.putBytes(data)
        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation storageRef.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                usuario.foto = task.result.toString()
                Toast.makeText(this, "Foto subida", Toast.LENGTH_SHORT).show()
                ActualizarUsuario(usuario)
            }
        }
    }

    private fun GuardarCambios(usuario: Usuario) {

        var nivel_usuario = nivel
        var tipo_usuairo = tipo
        var celular_usuaio = celular
        var observaciones_usuario = observaciones

        if (!vacio(celular_usuaio)||celular_usuaio.length()!=9){
            usuario.celular = celular_usuaio.text.toString()
        }



        usuario.tipo = tipo_usuairo.selectedItem.toString()
        usuario.nivel = nivel_usuario.selectedItem.toString()

        if (vacio(observaciones_usuario)){
            usuario.observaciones = "Ninguna"
        }else{
            usuario.observaciones = observaciones_usuario.text.toString()
        }

        if (filePath != null){
            Subirfoto(usuario)
        } else {
            ActualizarUsuario(usuario)
        }

    }

    fun Regresar(){
        val intent = Intent()
        intent.setClass(this, SesionActivity::class.java)
        startActivity(intent)
    }

    fun OnUserDataFinished(usuario: Usuario) {
        nombre.text = usuario.nombre
        codigo.text = usuario.codigo
        celular.setText(usuario.celular)
        var tipo = findViewById<Spinner>(R.id.tipo)
        var nivel = findViewById<Spinner>(R.id.nivel)

        for ( i in niveles.indices){
            if (usuario.nivel.toString() == niveles[i])
                nivel.setSelection(i)
        }

        for ( i in tipos.indices){
            if (usuario.tipo.toString() == tipos[i])
                tipo.setSelection(i)
        }

        observaciones.setText(usuario.observaciones)

        val url = usuario.foto
        Picasso.get().load(url).resize(350,350).into(foto)

        val but_cambiar_foto = findViewById<Button>(R.id.btn_cambiar_foto)
        but_cambiar_foto.setOnClickListener { CambiarFoto() }

        val but_guardar = findViewById<Button>(R.id.btn_guardar_modificacion)
        but_guardar.setOnClickListener { GuardarCambios(usuario) }

    }

    // FUNCION PARA MOSTRAR LA FOTO EN LA PANTALLA
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === PICK_IMAGE_REQUEST && resultCode === Activity.RESULT_OK
            && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                foto.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun CambiarFoto(){

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)

    }

}
