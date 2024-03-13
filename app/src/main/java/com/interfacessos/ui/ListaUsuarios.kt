package com.interfacessos.ui

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.interfacessos.database.DBHelper
import com.interfacessos.databinding.ActivityListaUsuariosBinding
import com.interfacessos.model.Usuario

class ListaUsuarios : AppCompatActivity() {

    private lateinit var binding: ActivityListaUsuariosBinding
    private lateinit var listaUsuarios: ArrayList<Usuario>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("chamou","chamou")
        binding = ActivityListaUsuariosBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val dbHelper = DBHelper(this)
        listaUsuarios = dbHelper.getUsuarios()

        val adapter = ArrayAdapter(applicationContext,android.R.layout.simple_list_item_1,listaUsuarios)

        binding.listaUsuarios.adapter = adapter

    }
}