package com.example.piscisoftmobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*



class Texto ( val textox: String = "" )

class MainActivity : AppCompatActivity() {

    private fun actualizar(t1: Texto) {
        try {
            val db = FirebaseFirestore.getInstance()
            val ref = db.collection("colx").document("docx")
            ref.update("textox", t1.textox)
            Toast.makeText(this, "Texto actualizado", Toast.LENGTH_SHORT).show()
            //Task successful
        } catch (e: Throwable) {
            Toast.makeText(this, "Error en la actualización: ${e}", Toast.LENGTH_SHORT).show()
            //Manage error
        }
    }

    private fun ver() {
        try {
            val db = FirebaseFirestore.getInstance()
            val ref = db.collection("colx").document("docx")
            ref.get().addOnSuccessListener { documentSnapshot ->
                val t1 = documentSnapshot.toObject(Texto::class.java)
                textView.text = t1?.textox
                Toast.makeText(this, "Texto recuperado", Toast.LENGTH_SHORT).show()
            }
            //Task successful
        } catch (e: Throwable) {
            Toast.makeText(this, "Error en la visualización: ${e}", Toast.LENGTH_SHORT).show()
            //Manage error
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ver()

        btn_actualizar.setOnClickListener {
            if (editText.text.toString() != ""){
                val t1 = Texto(editText.text.toString())
                actualizar(t1)
                ver()

            } else {
                Toast.makeText(this, "Ingrese un nuevo texto!!!!", Toast.LENGTH_SHORT).show()
            }
        }


    }
}
