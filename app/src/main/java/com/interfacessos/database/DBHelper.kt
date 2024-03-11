package com.interfacessos.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.interfacessos.model.Contato
import com.interfacessos.model.Usuario

class DBHelper(context: Context): SQLiteOpenHelper(context, "sos.db",null,1) {


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE Usuario (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, email TEXT NOT NULL)")
        db?.execSQL("CREATE TABLE Contato (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, telefone TEXT, id_usuario INTEGER NOT NULL, FOREIGN KEY (id_usuario) REFERENCES Usuario(id))")
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

    fun getUsuario(usuario: Usuario):Usuario{
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM Usuario WHERE nome=?", arrayOf(usuario.nome.toString()))

        if(c.count ==1){
            c.moveToFirst()

            val idIndex = c.getColumnIndex("id")
            val nomeIndex = c.getColumnIndex("nome")
            val emailIndex = c.getColumnIndex("email")
            val usuario = Usuario(
                id = c.getInt(idIndex),
                nome = c.getString(nomeIndex),
                email = c.getString(emailIndex),
                contatos = ArrayList<Contato>())
        }
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
        cursor.let {
            if (it != null) {
                if(it.moveToFirst()){
                    do{
                        val id = it.getInt(it.getColumnIndex("id"))
                        val nome = it.getString(it.getColumnIndex("nome"))
                        val telefone = it.getString(it.getColumnIndex("telefone"))

                        contatos.add(Contato(id,nome,telefone))
                    }while (it.moveToNext())
                }
            }
        }
        return contatos
    }
}