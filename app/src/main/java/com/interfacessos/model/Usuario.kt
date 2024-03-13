package com.interfacessos.model

class Usuario (
    val id: Int = 0,
    val nome: String = "",
    val email: String = ""
){
    override fun toString(): String {
        return "Usuario(id=$id, nome='$nome', email='$email')"
    }
}