package com.interfacessos.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.interfacessos.databinding.ActivityEtapaFinalBinding

class EtapaFinalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEtapaFinalBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEtapaFinalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val i = intent
        val bundle = i.extras
        val nome = i.extras?.getString("nome_usuario").toString()
        val email = i.extras?.getString("email_usuario").toString()
        val contato1 = i.extras?.getString("contato1_usuario").toString()
        val contato2 = i.extras?.getString("contato2_usuario").toString()

        binding.edtNome.setText(nome)
        binding.edtFinalEmail.setText(email)
        binding.edtFinalCont1.setText(contato1)
        binding.edtCont2.setText(contato2)

        binding.btnConfirmar.setOnClickListener {
            val nomeAlteracao = binding.edtNome.text
            val emailAlteracao = binding.edtFinalEmail.text
            val contato1Alteracao = binding.edtFinalCont1.text
            val contato2Alteracao = binding.edtCont2.text

            if(bundle != null){
                i.putExtras(bundle)
            }

            if(!nomeAlteracao.isBlank()&&!emailAlteracao.isBlank()&&!contato1Alteracao.isBlank()&&!contato2Alteracao.isBlank()){
                val i = Intent(this, TelaLocalizacaoActivity::class.java)
                i.putExtra("nome_usuario", nome)
                i.putExtra("email_usuario", email)
                i.putExtra("contato1_usuario",contato1)
                i.putExtra("contato2_usuario",contato2)

                startActivity(i)
            }
        }
    }
}