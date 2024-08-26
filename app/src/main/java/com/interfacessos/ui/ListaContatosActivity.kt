package com.interfacessos.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.interfacessos.R
import com.interfacessos.database.DBHelper
import com.interfacessos.databinding.ActivityListaContatosBinding
import com.interfacessos.model.Contato

class ListaContatosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaContatosBinding
    private lateinit var listaContatos: ArrayList<Contato>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityListaContatosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = DBHelper(this)
        listaContatos = dbHelper.getContatos()

        val adapter = ArrayAdapter(this, R.layout.item_lista, listaContatos)
        binding.listaContatos.adapter = adapter

        // Adicionar novo contato
        binding.buttonAddContato.setOnClickListener {
            val intent = Intent(this, AddEditContatoActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
