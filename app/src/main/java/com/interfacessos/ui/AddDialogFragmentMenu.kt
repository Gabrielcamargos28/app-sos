package com.interfacessos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.interfacessos.R
import com.interfacessos.database.DBHelper

class AddDialogFragmentMenu : DialogFragment() {

    private lateinit var dbHelper: DBHelper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bordas_arredondadas)

        return inflater.inflate(R.layout.dialog_menu_localizacao,container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DBHelper(requireContext())
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val btnSair = dialog?.findViewById<Button>(R.id.btn_sair_menu)
        btnSair?.setOnClickListener {
            dialog?.dismiss()
        }
        val btnDeletar = dialog?.findViewById<Button>(R.id.btn_deletar_dados)
        btnDeletar?.setOnClickListener {
                dbHelper.deleteDatabase(requireContext())
        }
    }
}