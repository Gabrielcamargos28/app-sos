package com.interfacessos.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.interfacessos.databinding.ActivityTerceiraEtapaBinding

class TerceiraEtapaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTerceiraEtapaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityTerceiraEtapaBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val i = intent
        val bundle = i.extras
        val nome = i.extras?.getString("nome_usuario")
        val email = i.extras?.getString("email_usuario")

        binding.btnSeta3etpSeguir.setOnClickListener{
            val contato1 = binding.edtContato.text.toString()
            val contato2 = binding.edtContato2.text.toString()

            if(bundle != null){
                i.putExtras(bundle)
            }
            val i = Intent(this, EtapaFinalActivity::class.java)
            i.putExtra("nome_usuario", nome)
            i.putExtra("email_usuario", email)
            i.putExtra("contato1_usuario",contato1)
            i.putExtra("contato2_usuario",contato2)
            if(contato1.length < 11 || contato2.length < 11) {
                    Toast.makeText(this, "Celular menor que 11 numeros", Toast.LENGTH_SHORT).show()
            }else{
                startActivity(i)
            }
        }
    }
}