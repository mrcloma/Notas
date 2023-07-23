package br.com.intelligencesoftware.notas;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import br.com.intelligencesoftware.notas.dao.NotasDAO;
import br.com.intelligencesoftware.notas.model.Nota;

public class MainActivity extends AppCompatActivity {

    ListView listaVisivel;
    Button btnNovoContato;
    Nota nota;
    NotasDAO notasDAO;
    ArrayList<Nota> arrayListPessoa;
    ArrayAdapter<Nota> arrayAdapterPessoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instanciando
        listaVisivel = (ListView)findViewById(R.id.listaPessoas);
        btnNovoContato = (Button)findViewById(R.id.btnNovoContato);

        registerForContextMenu(listaVisivel); //habilitando menu de contexto(clicar e segurar pra abrir opções)
        //ao clicar em Novo Contato
        btnNovoContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //redirecionando pra tela de preenchimanto do cadastro:
                Intent i = new Intent(MainActivity.this, FormNota.class);
                startActivity(i);
            }
        });

        //ao clicar em algum dos elementos da lista:
        listaVisivel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //pegando o elemento clicado e convertendo pra um obj da classe pessoa
                Nota pessoaEnviada = (Nota) arrayAdapterPessoa.getItem(position);
                //chamando a tela do formulario passando esses dados por parametro:
                Intent i = new Intent(MainActivity.this, FormNota.class);
                i.putExtra("nota-enviada", pessoaEnviada); //passando os dados do registro clicado
                startActivity(i);
            }
        });

        //ao clicar e segurar em algum dos elementos da lista:
        listaVisivel.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            //vai chamar o menu criado na linha 93
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                nota = arrayAdapterPessoa.getItem(position);//pegando o elemento
                return false;
            }
        });
    }

    //fazendo a consulta e populando a lista
    protected void popularListaPessoa(){
        notasDAO = new NotasDAO(MainActivity.this); //instanciando
        arrayListPessoa = notasDAO.selectAllNotas(); //atribuindo à listagem o resultado do select
        if (listaVisivel != null){
            //inserindo a listagem numa tela de exibição padrao:
            arrayAdapterPessoa = new ArrayAdapter<Nota>(MainActivity.this,android.R.layout.simple_list_item_1, arrayListPessoa);
            listaVisivel.setAdapter(arrayAdapterPessoa); //atribuindo o adaptador para exibicao
        }
        notasDAO.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        popularListaPessoa();
    }

    //criando o menu que aparece ao clicar e segurar:
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem menuItem = menu.add("Deletar"); //opção do menu que vai aparecer
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                notasDAO = new NotasDAO(MainActivity.this);
                long retornoDB = notasDAO.removerRegistro(nota);
                if (retornoDB ==-1){
                    alerta("Erro ao excluir.");
                }else{
                    alerta("Excluído com sucesso.");
                }
                //após excluir o registro, faz o select no banco novamente:
                popularListaPessoa();
                return false;
            }
        });
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    private void alerta(String mensagem){
        //funcao pra exibir mensagem básica ao usuario
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }
}