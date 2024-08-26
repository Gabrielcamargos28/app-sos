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

class DBHelper(context: Context): SQLiteOpenHelper(context, "sos.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE Usuario (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, email TEXT NOT NULL)")
        db?.execSQL("CREATE TABLE Contato (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, telefone TEXT, id_usuario INTEGER NOT NULL, FOREIGN KEY (id_usuario) REFERENCES Usuario(id))")
    }

    fun deleteDatabase(context: Context) {
        context.deleteDatabase("sos.db")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Exemplo de lógica de atualização: exclua e recrie tabelas ou execute scripts de atualização
        db?.execSQL("DROP TABLE IF EXISTS Usuario")
        db?.execSQL("DROP TABLE IF EXISTS Contato")
        onCreate(db) // Recrie as tabelas
    }

    fun insertUsuario(usuario: Usuario): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("nome", usuario.nome)
            put("email", usuario.email)
        }
        val res = db.insert("Usuario", null, contentValues)
        db.close()
        return res
    }

    fun insertContato(contato: Contato, idUsuario: Int): Long {
        Log.d("Insert Contato", "insert contato")
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("nome", contato.nome)
            put("telefone", contato.telefone)
            put("id_usuario", idUsuario)
        }
        val res = db.insert("Contato", null, contentValues)
        db.close()
        return res
    }

    @SuppressLint("Recycle")
    fun getUsuario(nomeUsuario: String): Usuario? {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM Usuario WHERE nome=?", arrayOf(nomeUsuario))
        val usuario: Usuario? = cursor.use {
            if (it.moveToFirst()) {
                val idIndex = it.getColumnIndex("id")
                val nomeIndex = it.getColumnIndex("nome")
                val emailIndex = it.getColumnIndex("email")
                Usuario(
                    id = it.getInt(idIndex),
                    nome = it.getString(nomeIndex),
                    email = it.getString(emailIndex)
                )
            } else {
                null
            }
        }
        db.close()
        return usuario
    }

    fun login(): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM Usuario", null)
        val isLoggedIn = cursor.count > 0
        cursor.close()
        db.close()
        return isLoggedIn
    }

    @SuppressLint("Range")
    fun getContatosDoUsuario(idUsuario: Int): ArrayList<Contato> {
        val contatos = ArrayList<Contato>()
        val db = this.readableDatabase
        val query = "SELECT * FROM Contato WHERE id_usuario = ?"
        val cursor: Cursor? = db.rawQuery(query, arrayOf(idUsuario.toString()))

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndex("id"))
                val nome = it.getString(it.getColumnIndex("nome"))
                val telefone = it.getString(it.getColumnIndex("telefone"))
                contatos.add(Contato(id, nome, telefone))
            }
        }
        db.close()
        return contatos
    }

    @SuppressLint("Range")
    fun getUsuarios(): ArrayList<Usuario> {
        val usuarios = ArrayList<Usuario>()
        val db = this.readableDatabase
        val query = "SELECT * FROM Usuario"
        val cursor: Cursor = db.rawQuery(query, null)

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndex("id"))
                val nome = it.getString(it.getColumnIndex("nome"))
                val email = it.getString(it.getColumnIndex("email"))
                usuarios.add(Usuario(id, nome, email))
            }
        }
        db.close()
        return usuarios
    }

    @SuppressLint("Range")
    fun getContatos(): ArrayList<Contato> {
        val contatos = ArrayList<Contato>()
        val db = this.readableDatabase
        val query = "SELECT * FROM Contato"
        val cursor: Cursor = db.rawQuery(query, null)

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndex("id"))
                val nome = it.getString(it.getColumnIndex("nome"))
                val telefone = it.getString(it.getColumnIndex("telefone"))
                val idUsuario = it.getInt(it.getColumnIndex("id_usuario"))
                contatos.add(Contato(id, nome, telefone, idUsuario))
            }
        }
        db.close()
        return contatos
    }
    @SuppressLint("Range")
    fun getContatoPorId(idContato: Int): Contato? {
        val db = this.readableDatabase
        // Inclua o idContato na cláusula WHERE
        val query = "SELECT * FROM Contato WHERE id = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(idContato.toString()))

        var contato: Contato? = null
        cursor.use {
            if (it.moveToFirst()) {
                val id = it.getInt(it.getColumnIndex("id"))
                val nome = it.getString(it.getColumnIndex("nome"))
                val telefone = it.getString(it.getColumnIndex("telefone"))
                val idUsuario = it.getInt(it.getColumnIndex("id_usuario"))
                contato = Contato(id, nome, telefone, idUsuario)
            }
        }
        db.close()
        return contato
    }

    fun updateContato(contato: Contato): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("nome", contato.nome)
            put("telefone", contato.telefone)
            // Nota: Não atualize o ID
        }
        val whereClause = "id = ?"
        val whereArgs = arrayOf(contato.id.toString())

        val result = db.update("Contato", values, whereClause, whereArgs)
        db.close()

        // Retorna true se a atualização foi bem-sucedida
        return result > 0
    }

}
