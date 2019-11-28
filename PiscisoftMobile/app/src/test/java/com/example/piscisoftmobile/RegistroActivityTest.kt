package com.example.piscisoftmobile

import android.widget.EditText
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class RegistroActivityTest {

    val registroActivity = RegistroActivity()

    @Before
    fun setUp() {
    }

    @Test
    fun vacioTest(){
        val resultado = registroActivity.vacio("")
        assertEquals(true,resultado)
    }

    @After
    fun tearDown() {
    }
}