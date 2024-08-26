package com.interfacessos.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.interfacessos.R
import com.interfacessos.database.DBHelper
import com.interfacessos.model.Contato
import com.interfacessos.model.Usuario

class AddEditContatoActivity : AppCompatActivity() {

    private lateinit var nomeEditText: EditText
    private lateinit var telefoneEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_contato)

        nomeEditText = findViewById(R.id.edit_nome)
        telefoneEditText = findViewById(R.id.edit_telefone)
        saveButton = findViewById(R.id.button_save)
        dbHelper = DBHelper(this)

        // Receber o ID do usuário do Intent
        val usuarioId = intent.getIntExtra("USUARIO_ID", -1)

        // Verificar se o ID do usuário é válido
        if (usuarioId == -1) {
            Toast.makeText(this, "ID do usuário não encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val contatoId = intent.getIntExtra("CONTATO_ID", -1)
        if (contatoId != -1) {
            // Edição
            val contato = dbHelper.getContatoPorId(contatoId)
            if (contato != null) {
                nomeEditText.setText(contato.nome)
                telefoneEditText.setText(contato.telefone)
                saveButton.setOnClickListener {
                    contato.nome = nomeEditText.text.toString()
                    contato.telefone = telefoneEditText.text.toString()
                    dbHelper.updateContato(contato)
                    Toast.makeText(this, "Contato atualizado com sucesso", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                Toast.makeText(this, "Contato não encontrado", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            // Adição
            saveButton.setOnClickListener {
                val nome = nomeEditText.text.toString()
                val telefone = telefoneEditText.text.toString()
                val novoContato = Contato(nome = nome, telefone = telefone, id_usuario = usuarioId)
                val usuario = getUsuario();
                val result = dbHelper.insertContato(novoContato, usuario.id)
                if (result != -1L) {
                    Toast.makeText(this, "Contato adicionado com sucesso", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Erro ao adicionar contato", Toast.LENGTH_SHORT).show()
                }
                finish()
            }
        }
    }
    fun getUsuario(): Usuario {
        var listaUsuarios = dbHelper.getUsuarios()
        return listaUsuarios[0];
    }
}
