package com.interfacessos.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.interfacessos.database.DBHelper
import com.interfacessos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        dbHelper = DBHelper(this)


        if(dbHelper.login()){
            val i = Intent(this, TelaLocalizacaoActivity::class.java)
            Log.d("Entrou no login", "foi para telaLocalizacao")
            startActivity(i)
        }else{
            Log.d("Entrou no Cadastro", "esta no Cadastro")
            setContentView(binding.root)
        }

        binding.btnSetaSeguir.setOnClickListener{
            val nome = binding.edtNome.text.toString()
            val i = Intent(this, SegundaEtapaActivity::class.java)
            i.putExtra("nome_usuario", nome)
            if(!nome.isBlank()){
                startActivity(i)
            }
        }
    }
}