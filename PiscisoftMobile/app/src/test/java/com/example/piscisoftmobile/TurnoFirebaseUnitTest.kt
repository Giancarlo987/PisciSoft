package com.example.piscisoftmobile

import android.util.Log
import com.example.piscisoftmobile.Model.Turno
import com.example.piscisoftmobile.Model.TurnoFirebase
import com.example.piscisoftmobile.Model.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await
import org.junit.Assert
import org.junit.Test

class TurnoFirebaseUnitTest {

    val turnoFirebase = TurnoFirebase()

    @Test
    fun turnoLlenoTest(){
        val resultado = turnoFirebase.turnoLleno(0)
        Assert.assertEquals(true, resultado)
    }

    @Test
    fun turnoNoLlenoTest(){
        val resultado = turnoFirebase.turnoLleno(15)
        Assert.assertEquals(false, resultado)
    }

    @Test
    fun turnoCaducadoTest(){
        val resultado = turnoFirebase.turnoCaducado("15:30","2019-11-11")
        Assert.assertEquals(true, resultado)
    }

    @Test
    fun turnoNoCaducadoTest(){
        val resultado = turnoFirebase.turnoCaducado("15:30","2019-12-14")
        Assert.assertEquals(false, resultado)
    }


}