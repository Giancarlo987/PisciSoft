package com.example.piscisoftmobile
import com.example.piscisoftmobile.Model.ReservaFirebase
import org.junit.Test
import org.junit.Assert.*

class ReservaFirebaseUnitTest: OnDataFinishedListener  {

    val reservaFirebase = ReservaFirebase()

    @Test
    fun reservaInasistidaTest(){
        val resultado = reservaFirebase.reservaInasistida("15:30","2019-11-26")
        assertEquals(true,resultado)
    }

    @Test
    fun reservaNoInasistidaTest(){
        val resultado = reservaFirebase.reservaInasistida("15:30","2019-12-14")
        assertEquals(false,resultado)
    }

}