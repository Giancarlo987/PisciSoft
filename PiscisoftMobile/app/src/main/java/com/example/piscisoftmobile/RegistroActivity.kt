package com.example.piscisoftmobile

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
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class RegistroActivity : AppCompatActivity() {

    private lateinit var codigo: String
    private lateinit var etCodigo: EditText

    private lateinit var password: String
    private lateinit var etPassword: EditText

    private lateinit var repetirPassword: String
    private lateinit var etRepetirPassword: EditText

    private lateinit var iviFoto: ImageView
    private lateinit var btnSubirFoto: Button

    private lateinit var loadingScreen: FrameLayout

    private lateinit var padreView: ScrollView

    private lateinit var continuarButton: Button

    //----------------------------------------------------------------------------------------------

    private lateinit var storageRef: StorageReference
    private var filePath: Uri? = null

    private val PICK_IMAGE_REQUEST = 71

    //----------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        etCodigo = findViewById(R.id.etCodigo)
        etPassword = findViewById(R.id.etPassword)
        etRepetirPassword = findViewById(R.id.etRepetirPassword)
        btnSubirFoto = findViewById(R.id.butUploadFoto)
        continuarButton = findViewById(R.id.btnRegistrarse)
        iviFoto = findViewById(R.id.iviFoto)
        loadingScreen = findViewById(R.id.loadingScreen)
        padreView = findViewById(R.id.padreView)

        // BOTON PARA SUBIR UNA FOTO DE PERFIL
        btnSubirFoto.setOnClickListener {uploadFoto()}

        //BOTON PARA REGISTRARSE
        continuarButton.setOnClickListener { registrar() }
    }
    // FUNCION PARA ESCRIBIR UN USUARIO EN LA DB
    private fun writeNewUser(codigo: String, password: String, foto: String) {
        // Datos del usuario faltantes

        var estado: String? = null
        var tipo: String? = null
        var observaciones: String? = null
        var celular: String? = null
        var inasistencias: Long = 0
        var nivel: String? = null
        var nombre: String? = null

        // Crear un usuario para registrarlo en la DB
        val user = Usuario(
            codigo,
            estado,
            password,
            tipo,
            observaciones,
            celular,
            inasistencias,
            nivel,
            nombre,
            foto
        )
        // Enviar los datos a la DB
        val db = FirebaseFirestore.getInstance()
        db.collection("usuario").document(codigo).set(user)
        Toast.makeText(this, "Registro Existoso", Toast.LENGTH_SHORT).show()
        padreView.visibility = View.VISIBLE
        loadingScreen.visibility = View.GONE
    }
    // FUNCION PARA MOSTRAR LA FOTO EN LA PANTALLA
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
    // FUNCION PARA OBTENER LAS CREDENCIALES--------------------------------------------------------
    private fun registrar(){
        padreView.visibility = View.GONE
        loadingScreen.visibility = View.VISIBLE

        if (!etCodigo.text.toString().contentEquals("")) {
            codigo = etCodigo.text.toString()
        } else {
            Toast.makeText(this, "Por favor, ingrese usuario..", Toast.LENGTH_SHORT).show()
            return
        }

        if (!etPassword.text.toString().contentEquals("")) {
            password = etPassword.text.toString()
        } else {
            Toast.makeText(this, "Por favor, ingrese contraseña..", Toast.LENGTH_SHORT).show()
            return
        }

        if (!etRepetirPassword.text.toString().contentEquals("")) {
            repetirPassword = etRepetirPassword.text.toString()
        } else {
            Toast.makeText(this, "Por favor, repita la contraseña..", Toast.LENGTH_SHORT).show()
            return
        }

        if (!password.contentEquals(repetirPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        } else {
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            storageRef =
                FirebaseStorage.getInstance().reference.child("profiles").child(codigo + "_" + timeStamp)

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
                    verificar(codigo,password,task.result.toString())
                    regresar()
                } else {
                    padreView.visibility = View.VISIBLE
                    loadingScreen.visibility = View.GONE
                }
            }
        }

        //verificar(usuario,contrasena)
    }
    // FUNCION DE VERIFICAR DE USUARIO--------------------------------------------------------------
    private fun verificar(codigo:String, password: String, foto: String) {
        // Buscar usuario con codigo similar
        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("usuario")
        val query = ref.whereEqualTo("codigo",codigo)
        // Resultado de la busqueda
        query.get()
            .addOnSuccessListener { documents ->
                if ( ! documents.isEmpty ) {
                    // Si encuentra usuario, muestra alerta
                    Toast.makeText(this, "Usuario existente", Toast.LENGTH_SHORT).show()
                } else {
                    // Si no encuentra, escribe en la BD
                    Toast.makeText(this, "Conectando...", Toast.LENGTH_SHORT).show()
                    writeNewUser(codigo,password,foto)
                    finish()
                }
            }
            .addOnFailureListener{
                Toast.makeText(this, "Error en Firebase", Toast.LENGTH_SHORT).show()
            }
    }
    // FUNCION PARA SUBIR FOTO DESDE EL DISPOSITIVO-------------------------------------------------
    private fun uploadFoto() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun regresar(){
        val intent = Intent()
        intent.setClass(this, MainActivity::class.java)
        startActivityForResult(intent,1)
    }
}
