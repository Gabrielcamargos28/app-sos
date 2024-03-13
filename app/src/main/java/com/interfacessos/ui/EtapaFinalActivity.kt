package com.interfacessos.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.interfacessos.database.DBHelper
import com.interfacessos.databinding.ActivityEtapaFinalBinding
import com.interfacessos.model.Contato
import com.interfacessos.model.Usuario

class EtapaFinalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEtapaFinalBinding
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEtapaFinalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        //puxando
        val i = intent
        val bundle = i.extras
        val nome = i.extras?.getString("nome_usuario").toString()
        val email = i.extras?.getString(    "email_usuario").toString()
        val contato1 = i.extras?.getString("contato1_usuario").toString()
        val contato2 = i.extras?.getString("contato2_usuario").toString()

        //exibindo
        binding.edtNome.setText(nome)
        binding.edtFinalEmail.setText(email)
        binding.edtFinalCont1.setText(contato1)
        binding.edtCont2.setText(contato2)

        //confirmando alteracoes
        binding.btnConfirmar.setOnClickListener {
            val nomeAlteracao = binding.edtNome.text.toString()
            val emailAlteracao = binding.edtFinalEmail.text.toString()
            val contato1Alteracao = binding.edtFinalCont1.text.toString()
            val contato2Alteracao = binding.edtCont2.text.toString()

            //criando usuario
            val usuarioCriado = Usuario(0,nomeAlteracao,emailAlteracao)

            Log.d("nomecriado","${usuarioCriado.nome}")
            //persistindo usuario
            insertUsuario(usuarioCriado)
            val usuarioRecuperado: Usuario? = dbHelper.getUsuario(nomeAlteracao)
            val id = usuarioRecuperado!!.id

            //teste contato

            val contatoCriado1 = Contato(1,"teste2",contato1Alteracao,id)

            val contatoCriado2 = Contato(1,"teste2",contato2Alteracao,id)

            insertContato(contatoCriado1,id)
            insertContato(contatoCriado2,id)

            val contatos: ArrayList<Contato> = dbHelper.getContatosDoUsuario(usuarioCriado.id)

            val contatoRecuperado = contatos.forEach{
                Log.d("Contato Recuperado Nome","${it.nome}")
            }
            Log.d("TESTE1","${contatoRecuperado}")

            if(bundle != null){
                i.putExtras(bundle)
            }

            if(!nomeAlteracao.isBlank()&&!emailAlteracao.isBlank()&&!contato1Alteracao.isBlank()&&!contato2Alteracao.isBlank()){
                val i = Intent(this, TelaLocalizacaoActivity::class.java)
                //emviando dados para TelaLocalizacao

                i.putExtra("nome_usuario_alterado", nomeAlteracao)
                i.putExtra("email_usuario_alterado", emailAlteracao)
                i.putExtra("contato1_usuario_alterado",contato1Alteracao)
                i.putExtra("contato2_usuario_alterado",contato2Alteracao)

                startActivity(i)
            }
        }
    }
    fun insertUsuario(usuario: Usuario){
        if(usuario != null){
            dbHelper.insertUsuario(usuario)
            Log.d("Entrou no insertUsuario","instancia dbHelper")
        }else{
            Toast.makeText(this, "Usuario Null", Toast.LENGTH_SHORT).show()
        }
    }
    fun insertContato(contato: Contato, id: Int){
        if(contato!=null){
            dbHelper.insertContato(contato, id)
            Log.d("Entrou no insertUsuario","instancia dbHelper")
        }else{
            Toast.makeText(this, "Usuario Null", Toast.LENGTH_SHORT).show()
        }
    }
}