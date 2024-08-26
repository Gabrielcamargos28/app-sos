package com.interfacessos.model

class Contato(val id: Int=0, var nome: String = "", var telefone:String = "", val id_usuario: Int=0) {

    override fun toString(): String {
        return "Contato \nid=$id\nNome='$nome'\nTelefone='$telefone'"
    }
}