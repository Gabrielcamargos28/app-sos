package com.interfacessos.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.interfacessos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



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