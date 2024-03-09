package com.interfacessos.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.interfacessos.databinding.ActivitySegundaEtapaBinding

class SegundaEtapaActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySegundaEtapaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySegundaEtapaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val i = intent
        val bundle = i.extras
        val nome = i.extras?.getString("nome_usuario")

        binding.btnSeta2etpSeguir.setOnClickListener{
            val email = binding.edtEmail.text.toString()

            val i = Intent(this, TerceiraEtapaActivity::class.java)
            if(bundle != null){
                i.putExtras(bundle)
            }
            i.putExtra("nome_usuario", nome)
            i.putExtra("email_usuario", email)
            if(!email.isBlank() && !email.isBlank()){
                startActivity(i)
            }
        }
    }
}