package com.example.piscisoftmobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment


class PerfilFragment : Fragment() {


    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_perfil, container, false)
        val btn_modificar: Button = root.findViewById(R.id.btn_modificar)
        btn_modificar.setOnClickListener { ir_modificar() }
        mContext = root.context
        return root

    }

    private fun ir_modificar(){
        val intent : Intent = Intent()
        intent.setClass(mContext, ModificarActivity::class.java)
        startActivity(intent)
    }
}