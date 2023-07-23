package br.com.intelligencesoftware.notas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.com.intelligencesoftware.notas.dao.NotasDAO;
import br.com.intelligencesoftware.notas.model.Nota;

/**
 * Created by Lanterna Verde on 27/08/2017.
 */

public class FormNota extends AppCompatActivity {

    //criando objetos
    EditText editTitulo, editNota;
    Button btnVariavel;
    Nota nota;
    Nota notaEditar; //pessoa caso novo registro, pessoaEditar caso editar contato
    NotasDAO notasDAO;
    long retorno;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);

        Intent i = getIntent(); //buscando alguma entidade que ja foi utilizada (pessoaEditar)
        notaEditar = (Nota) i.getSerializableExtra("nota-enviada"); //atribuindo ao objeto de edicao com uma string de identificacao
        nota = new Nota();
        notasDAO = new NotasDAO(FormNota.this); //passando o contexto atual

        //instanciando os objetos visuais
        editTitulo = (EditText)findViewById(R.id.editTitulo);
        editNota = (EditText)findViewById(R.id.editNota);
        btnVariavel = (Button) findViewById(R.id.btnVariavel);

        //verificando se o objeto pessoaEditar possui valores, se sim, é edição, se nao, é novo contato:
        if(notaEditar != null){ //é edição
            btnVariavel.setText("Salvar Edição");

            //preenchendo os dados do formulario com os registros oriundos do item clicado na listagem
            editTitulo.setText(notaEditar.getTitulo());
            editNota.setText(notaEditar.getNota());
            nota.setId(notaEditar.getId()); //passando o ID já existente

        }else{ //é novo registro
            btnVariavel.setText("Salvar Novo");
        }
        //ao clicarem no botao:
        btnVariavel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //atribuindo os valores digitados
                nota.setTitulo(editTitulo.getText().toString());
                nota.setNota(editNota.getText().toString());

                //verificando se é novo contato ou atualização de contato
                if(notaEditar != null){ //atualizar contato
                    retorno = notasDAO.atualizarNota(nota);
                }else{ //salvar novo contato
                    retorno = notasDAO.salvarNota(nota); //chamando a funcao que realiza o cadastro
                }
                //analisando o resultado do insert ou do update:
                if (retorno == -1){
                    alerta("Erro ao interagir com BD.");
                }else{
                    alerta("Sucesso.");
                }
                notasDAO.close(); //fechando
                finish();
            }
        });
    }

    private void alerta(String mensagem){
        //funcao pra exibir mensagem básica ao usuario
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }
}