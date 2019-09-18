package com.example.piscisoftmobile.views

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import com.example.piscisoftmobile.Model.Usuario
import com.example.piscisoftmobile.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_main.btn_registrarse
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class RegistroActivity : AppCompatActivity() {

    private lateinit var usuario: String
    private lateinit var et_usuario: EditText

    private lateinit var contrasena: String
    private lateinit var et_contrasena: EditText

    private lateinit var repetirContra: String
    private lateinit var et_repetirContra: EditText

    private lateinit var iviFoto: ImageView
    private lateinit var btn_subirFoto: Button

    private lateinit var loading_screen: FrameLayout

    private lateinit var padreView: ScrollView

    private lateinit var continuarButton: Button

    //-----------------------------------

    private lateinit var storageRef: StorageReference
    private var filePath: Uri? = null

    private val PICK_IMAGE_REQUEST = 71

    //-----------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        et_usuario = findViewById(R.id.et_usuario)
        et_contrasena = findViewById(R.id.et_contra)
        et_repetirContra = findViewById(R.id.et_repetirContra)
        btn_subirFoto = findViewById(R.id.but_uploadFoto)
        continuarButton = findViewById(R.id.btn_registrarse)
        iviFoto = findViewById(R.id.iviFoto)
        loading_screen = findViewById(R.id.loading_screen)
        padreView = findViewById(R.id.padreView)

        // BOTON PARA SUBIR UNA FOTO DE PERFIL
        btn_subirFoto.setOnClickListener {uploadFoto()}

        //BOTON PARA REGISTRARSE
        btn_registrarse.setOnClickListener { registrar() }
    }

    private fun writeNewUser(codigo: String, password: String, foto: String) {

        var nombre: String = ""
        var cod: String = codigo
        var celular : String = ""
        var estado : String = ""
        var inasistencias : Number = 0
        var nivel: String = ""
        var pass: String = password
        var tipo: String = ""
        var imagen: String = foto

        val user = Usuario(
            nombre,
            cod,
            celular,
            estado,
            inasistencias,
            nivel,
            pass,
            tipo,
            imagen
        )
        val db = FirebaseFirestore.getInstance()
        db.collection("usuario").document(cod).set(user)
        Toast.makeText(this, "Registro Existoso", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === PICK_IMAGE_REQUEST && resultCode === Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                iviFoto.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun registrar(){
        padreView.visibility = View.GONE
        loading_screen.visibility = View.VISIBLE

        if (!et_usuario.text.toString().contentEquals("")) {
            usuario = et_usuario.text.toString()
        } else {
            Toast.makeText(this, "Por favor, ingrese usuario..", Toast.LENGTH_SHORT).show()
            return
        }

        if (!et_contrasena.text.toString().contentEquals("")) {
            contrasena = et_contrasena.text.toString()
        } else {
            Toast.makeText(this, "Por favor, ingrese contraseña..", Toast.LENGTH_SHORT).show()
            return
        }

        if (!et_repetirContra.text.toString().contentEquals("")) {
            repetirContra = et_repetirContra.text.toString()
        } else {
            Toast.makeText(this, "Por favor, repita la contraseña..", Toast.LENGTH_SHORT).show()
            return
        }

        if (!contrasena.contentEquals(repetirContra)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        } else {
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            storageRef =
                FirebaseStorage.getInstance().reference.child("profiles").child(usuario + "_" + timeStamp)

            iviFoto.isDrawingCacheEnabled = true
            iviFoto.buildDrawingCache()

            val bitmap = (iviFoto.drawable as BitmapDrawable).bitmap
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
                    verificar(usuario,contrasena,task.result.toString())
                } else {
                    padreView.visibility = View.VISIBLE
                    loading_screen.visibility = View.GONE
                }
            }
        }

        //verificar(usuario,contrasena)
    }

    private fun verificar(codigo:String, password: String, foto: String) {

        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("usuario")
        val query = ref.whereEqualTo("codigo",codigo)

        query.get()
            .addOnSuccessListener { documents ->
                if ( ! documents.isEmpty ) {
                    Toast.makeText(this, "Usuario existente", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, "Conectando...", Toast.LENGTH_SHORT).show()
                    writeNewUser(codigo,password,foto)
                }
            }
            .addOnFailureListener{
                Toast.makeText(this, "Error en Firebase", Toast.LENGTH_SHORT).show()
            }
    }
    private fun uploadFoto() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }
}
