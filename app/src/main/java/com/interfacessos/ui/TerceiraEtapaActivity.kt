package com.interfacessos.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
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

        binding.edtContato.addTextChangedListener(PhoneNumberTextWatcher(binding.edtContato))
        binding.edtContato2.addTextChangedListener(PhoneNumberTextWatcher(binding.edtContato2))

        binding.btnSeta3etpSeguir.setOnClickListener{
            val contato1 = binding.edtContato.text.toString()
            val contato2 = binding.edtContato2.text.toString()

            if(bundle != null){
                i.putExtras(bundle)
            }
            val intent = Intent(this, EtapaFinalActivity::class.java)
            intent.putExtra("nome_usuario", nome)
            intent.putExtra("email_usuario", email)
            intent.putExtra("contato1_usuario",contato1)
            intent.putExtra("contato2_usuario",contato2)
            if(contato1.length < 11 || contato2.length < 11) {
                Toast.makeText(this, "Celular menor que 11 números", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(intent)
            }
        }
    }

    private inner class PhoneNumberTextWatcher(private val editText: EditText) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            // Remove o TextWatcher para evitar chamadas recursivas
            editText.removeTextChangedListener(this)

            // Formatar o texto
            val formattedText = formatPhoneNumber(s.toString())

            // Definir o texto formatado de volta no EditText
            editText.setText(formattedText)

            // Mover o cursor para o final do texto
            editText.setSelection(formattedText.length)

            // Adicionar o TextWatcher de volta
            editText.addTextChangedListener(this)
        }

        private fun formatPhoneNumber(phoneNumber: String): String {
            // Remove qualquer caractere não numérico
            val digits = phoneNumber.replace("\\D".toRegex(), "")

            // Formatação específica para (XX) XXXXX-XXXX
            return when {
                digits.length >= 2 && digits.length < 7 -> "(${digits.substring(0, 2)}) ${digits.substring(2)}"
                digits.length >= 7 && digits.length < 11 -> "(${digits.substring(0, 2)}) ${digits.substring(2, 7)}-${digits.substring(7)}"
                digits.length >= 11 -> "(${digits.substring(0, 2)}) ${digits.substring(2, 7)}-${digits.substring(7, 11)}"
                else -> digits
            }
        }
    }
}
