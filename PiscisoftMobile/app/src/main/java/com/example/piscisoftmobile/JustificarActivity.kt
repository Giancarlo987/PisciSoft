package com.example.piscisoftmobile

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.net.toFile
import com.example.piscisoftmobile.Model.Justificacion
import com.example.piscisoftmobile.Model.Reserva
import com.example.piscisoftmobile.Model.Turno
import kotlinx.android.synthetic.main.activity_justificar.*
import kotlinx.android.synthetic.main.activity_registro.*
import java.io.File
import java.io.IOException
import android.R.attr.data
import android.app.PendingIntent.getActivity
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class JustificarActivity : AppCompatActivity() {

    lateinit var reserva : Reserva
    lateinit var turno : Turno
    lateinit var justificacion : Justificacion
    var PICK_IMAGE_REQUEST = 71
    lateinit var bitmap : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_justificar)
        reserva = intent.getSerializableExtra("reserva") as Reserva
        turno = intent.getSerializableExtra("turno") as  Turno
        justificacion = Justificacion()

        btn_buscar.setOnClickListener{adjuntarEvidencia()}

        btn_enviar.setOnClickListener{enviarJustificacion()}
    }


    fun adjuntarEvidencia() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === PICK_IMAGE_REQUEST && resultCode === Activity.RESULT_OK && data != null && data.data != null) {
            val uri = data.data
            try {
                var displayName: String?
                if (uri.toString().startsWith("content://")) {
                    var cursor: Cursor? = null
                    try {
                        cursor =
                            this.getContentResolver().query(uri!!, null, null, null, null)
                        if (cursor != null && cursor!!.moveToFirst()) {
                            displayName =
                                cursor!!.getString(cursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                            tv_nombreEvidencia.text = displayName
                        }
                    } finally {
                        cursor!!.close()
                    }
                }
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)


                //iviFoto.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun enviarJustificacion() {
        if (et_motivo.text.toString() != "" && tv_nombreEvidencia.text!= "evidencia.jpg") {
            justificacion.codReserva = reserva.codReserva
            val formato = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            justificacion.fechaEnvio = LocalDate.now().format(formato)
            justificacion.motivo = et_motivo.text.toString()
            justificacion.estado = "Enviada"
            subirEvidencia()


        } else {
            Toast.makeText(this, "Por favor, agregue los datos necesarios", Toast.LENGTH_SHORT).show()
        }
    }

    fun subirEvidencia() {
        lateinit var storageRef: StorageReference

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        storageRef = FirebaseStorage.getInstance().reference.child("evidence").child(reserva.codReserva + "_" + timeStamp)

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 150, baos)
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
                justificacion.fotoDocumento = task.result.toString()
                FirebaseFirestore.getInstance().collection("justificacion").add(justificacion)
            }
        }
    }

}
