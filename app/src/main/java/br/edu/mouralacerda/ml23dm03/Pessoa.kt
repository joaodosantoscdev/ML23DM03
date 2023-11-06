package br.edu.mouralacerda.ml23dm03

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Pessoa(
    var nome: String,
    var idade: Int,
    var curso: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Serializable {

    override fun toString(): String {
        return "ID: $id / NOME: $nome"
    }

}