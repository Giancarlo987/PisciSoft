package com.example.piscisoftmobile

import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class ModificarActivityTest {

    var modificarActivity = ModificarActivity()

    @Before
    fun setUp() {
    }

    @Test
    fun vacioTest(){
        val resultado = modificarActivity.vacio("")
        assertEquals(true,resultado)
    }

    @After
    fun tearDown() {
    }
}