package com.interfacessos.model

class Contato(val id: Int=0, val nome: String = "", val telefone:String = "", val id_usuario: Int=0) {

    override fun toString(): String {
        return "Contato(id=$id, nome='$nome', telefone='$telefone', id_usuario='${id_usuario}')"
    }
}