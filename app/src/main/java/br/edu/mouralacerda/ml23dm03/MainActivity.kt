package br.edu.mouralacerda.ml23dm03

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    var listaDaTela: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listaDaTela = findViewById(R.id.lstNomes)

        val botaoNovoNome = findViewById<FloatingActionButton>(R.id.btnNovoNome)

        botaoNovoNome.setOnClickListener {
            val i = Intent(this, CadastroNomes::class.java)
            startActivity(i)
        }

        listaDaTela!!.setOnItemLongClickListener { adapterView, view, i, l ->

            val alerta = AlertDialog.Builder(this)

            alerta
                .setTitle("Apagar Pessoa")
                .setMessage("Deseja realmente apagar esta pessoa da lista?")
                .setPositiveButton("Sim") { dialog, which ->
                    val p = adapterView.adapter.getItem(i) as Pessoa
                    Database.getInstance(this)!!.pessoaDAO().apagar(p)
                    atualizarLista("id")
                    Toast.makeText(this, "Pessoa apagada", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Não") { dialog, which ->
                    Toast.makeText(this, "Pessoa não apagada", Toast.LENGTH_SHORT).show()
                }
                .show()

            true
        }

        listaDaTela!!.setOnItemClickListener { adapterView, view, i, l ->

            val pacote = Bundle()
            val p = adapterView.adapter.getItem(i) as Pessoa
            pacote.putInt("pessoaId", p.id)

            val i = Intent(this, CadastroNomes::class.java)
            i.putExtra("pacote", pacote)
            startActivity(i)

            true
        }
    }

    override fun onResume() {
        super.onResume()

        atualizarLista()
    }

    fun atualizarLista(ordem: String? = null) {
        val sharedPreferences = getSharedPreferences("MinhasPreferencias", Context.MODE_PRIVATE)
        val ordem = ordem ?: sharedPreferences.getString("ordem", "id")

        var lista: List<Pessoa>? = null

        if (ordem.equals("id")) {
            lista = Database.getInstance(this)!!.pessoaDAO().listarPorId()
        } else if (ordem.equals("nome")) {
            lista = Database.getInstance(this)!!.pessoaDAO().listarPorNome()
        }
        val listaAdaptada = ArrayAdapter(this, android.R.layout.simple_list_item_1, lista!!)

        listaDaTela!!.adapter = listaAdaptada

        salvarOrdenacaoSelecionada(ordem!!)
    }

    fun salvarOrdenacaoSelecionada(ordem: String) {
        val sharedPreferences = getSharedPreferences("MinhasPreferencias", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("ordem", ordem)
        editor.apply()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_principal, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.ordemNome) {
            atualizarLista("nome")
        } else if(item.itemId == R.id.ordemId) {
            atualizarLista("id")
        }

        return super.onOptionsItemSelected(item)
    }
}

