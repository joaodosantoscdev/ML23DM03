package br.edu.mouralacerda.ml23dm03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner

class CadastroNomes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_nomes)

        val botaoSalvar = findViewById<Button>(R.id.btnSalvar)
        val edtNome = findViewById<EditText>(R.id.edtNome)
        val edtIdade = findViewById<EditText>(R.id.edtIdade)
        val edtCurso = findViewById<Spinner>(R.id.edtCurso)

        var pessoa: Pessoa? = null;


        val pacote = intent.getBundleExtra("pacote")
        if (pacote != null) {
            val pessoaId = pacote.getInt("pessoaId")
            pessoa = Database.getInstance(this)!!.pessoaDAO().pegarPorId(pessoaId)

            if (pessoa != null && pessoa.id > 0) {
                edtNome.setText(pessoa.nome)
                edtIdade.setText(pessoa.idade.toString())

                val cursosArray = resources.getTextArray(R.array.cursosArray)
                edtCurso.setSelection(cursosArray.indexOf(pessoa.curso))
            }
        }

        botaoSalvar.setOnClickListener {
            if (pessoa != null && pessoa.id > 0) {
                pessoa.nome = edtNome.text.toString()
                pessoa.idade = Integer.parseInt(edtIdade.text.toString())
                pessoa.curso = edtCurso.selectedItem.toString()
                Database.getInstance(this)!!.pessoaDAO().atualizar(pessoa)

                finish()
            } else {
                val p = Pessoa(edtNome.text.toString(), Integer.parseInt(edtIdade.text.toString()), edtCurso.selectedItem.toString())

                Database.getInstance(this)!!.pessoaDAO().salvar(p)

                finish()
            }
        }

    }
}