package com.interfacessos.ui

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.interfacessos.R
import com.interfacessos.database.DBHelper
import com.interfacessos.databinding.ActivityListaUsuariosBinding
import com.interfacessos.model.Usuario

class ListaUsuarios : AppCompatActivity() {

    private lateinit var binding: ActivityListaUsuariosBinding
    private lateinit var listaUsuarios: ArrayList<Usuario>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaUsuariosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = DBHelper(this)
        listaUsuarios = dbHelper.getUsuarios()

        // Use um layout personalizado se `Usuario` for um objeto complexo
        val adapter = ArrayAdapter(this, R.layout.item_lista, listaUsuarios)

        binding.listaUsuarios.adapter = adapter
    }
}
