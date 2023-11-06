package br.edu.mouralacerda.ml23dm03

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

@androidx.room.Database(entities = [Pessoa::class], version = 1)
abstract class Database: RoomDatabase() {

    abstract fun pessoaDAO(): PessoaDAO

    companion object {

        private var database: Database? = null
        private val DATABASE = "BDNOMES"

        fun getInstance(context: Context): Database? {
            if(database == null) {
                return criaBanco(context)
            } else {
                return database
            }
        }
        private fun criaBanco(context: Context): Database? {
            return Room.databaseBuilder(context,
                Database::class.java,
                DATABASE).allowMainThreadQueries().build()
        }
    }
}