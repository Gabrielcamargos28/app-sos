package com.interfacessos.ui

import android.os.Bundle
import android.widget.ArrayAdapter
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

        var um: Int = 1

        listaContatos = dbHelper.getContatos()

        val adapter = ArrayAdapter(applicationContext,android.R.layout.simple_list_item_1,listaContatos)

        binding.listaContatos.adapter = adapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}