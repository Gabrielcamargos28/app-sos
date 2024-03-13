package com.interfacessos.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.interfacessos.model.Contato
import com.interfacessos.model.Usuario

class DBHelper(context: Context): SQLiteOpenHelper(context, "sos.db",null,1) {


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE Usuario (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, email TEXT NOT NULL)")
        db?.execSQL("CREATE TABLE Contato (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, telefone TEXT, id_usuario INTEGER NOT NULL, FOREIGN KEY (id_usuario) REFERENCES Usuario(id))")
    }

    fun deleteDatabase(context: Context){
        context.deleteDatabase("sos.db")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }


    fun insertUsuario(usuario: Usuario): Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("nome", usuario.nome)
        contentValues.put("email", usuario.email)
        val res = db.insert("Usuario",null, contentValues)
        db.close()
        return res
    }
    fun insertContato(contato: Contato, idUsuario: Int):Long{
        Log.d("Insert Contato","insert contato")
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("nome", contato.nome)
        contentValues.put("telefone", contato.telefone)
        contentValues.put("id_usuario", idUsuario)
        val res = db.insert("Contato",null,contentValues)
        db.close()
        return res
    }

    @SuppressLint("Recycle")
    fun getUsuario(nomeUsuario: String): Usuario?{
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM Usuario WHERE nome=?", arrayOf(nomeUsuario))

        Log.d("TAG nome usuario", "${nomeUsuario}")
        var usuario: Usuario?=null

        //if (c.count == 1){
        if(c.moveToFirst()){
            val idIndex = c.getColumnIndex("id")
            val nomeIndex = c.getColumnIndex("nome")
            val emailIndex = c.getColumnIndex("email")
            usuario = Usuario(
                id = c.getInt(idIndex),
                nome = c.getString(nomeIndex),
                email = c.getString(emailIndex))
            Log.d("TAG", "id ${idIndex},nome ${nomeIndex}, email: ${emailIndex}")
        }else{
            Log.d("TAG", "Nenhum dado encontrado na tabela Usuario")
        }
        c.close()
        db.close()
        return usuario
    }
    fun login(): Boolean{

        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM Usuario",null)

        if(c.count == 1){
            db.close()
            return true
        }else{
            db.close()
            return false
        }
    }
    @SuppressLint("Range")
    fun getContatosDoUsuario(idUsuario: Int): ArrayList<Contato>{
        val contatos = ArrayList<Contato>()

        val db = this.readableDatabase

        val query = "SELECT * FROM Contato WHERE id_usuario = ?"

        val cursor: Cursor? = db.rawQuery(query, arrayOf(idUsuario.toString()))

        cursor?.let {
                if(it.moveToFirst()){
                    do{
                        val id = it.getInt(it.getColumnIndex("id"))
                        val nome = it.getString(it.getColumnIndex("nome"))
                        val telefone = it.getString(it.getColumnIndex("telefone"))

                        contatos.add(Contato(id,nome,telefone))
                    }while (it.moveToNext())
            }
        }
        return contatos
    }
    @SuppressLint("Range")
    fun getUsuarios(): ArrayList<Usuario> {
        val usuarios = ArrayList<Usuario>()
        val db = this.readableDatabase
        val query = "SELECT * FROM Usuario"
        val cursor: Cursor = db.rawQuery(query, null)

        cursor.moveToFirst() // Move para o primeiro resultado, se existir

        while (!cursor.isAfterLast) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val nome = cursor.getString(cursor.getColumnIndex("nome"))
            val email = cursor.getString(cursor.getColumnIndex("email"))

            usuarios.add(Usuario(id, nome, email))

            cursor.moveToNext() // Move para o próximo resultado
        }

        cursor.close() // Fecha o cursor após o uso

        return usuarios
    }
    @SuppressLint("Range")
    fun getContatos(): ArrayList<Contato> {
        val usuarios = ArrayList<Contato>()
        val db = this.readableDatabase
        val query = "SELECT * FROM Contato"
        val cursor: Cursor = db.rawQuery(query, null)

        cursor.moveToFirst() // Move para o primeiro resultado, se existir

        while (!cursor.isAfterLast) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val nome = cursor.getString(cursor.getColumnIndex("nome"))
            val telefone = cursor.getString(cursor.getColumnIndex("telefone"))
            val idUsuario = cursor.getInt(cursor.getColumnIndex("id_usuario"))

            usuarios.add(Contato(id, nome, telefone,idUsuario))

            cursor.moveToNext() // Move para o próximo resultado
        }
        cursor.close() // Fecha o cursor após o uso
        return usuarios
    }


}