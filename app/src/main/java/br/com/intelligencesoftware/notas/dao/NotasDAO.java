package br.com.intelligencesoftware.notas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import br.com.intelligencesoftware.notas.model.Nota;

/**
 * Created by Lanterna Verde on 27/08/2017.
 * classe que vai fazer todas as interações com o banco de dados
 */

public class NotasDAO extends SQLiteOpenHelper {
    //dados do banco:
    private static final String NOME_BANCO = "dbNotas.db";
    private static final int VERSAO = 4;
    private static final String TABELA = "notas";

    //colunas da tabela:
    private static final String ID = "id";
    private static final String TITULO = "titulo";
    private static final String NOTA = "nota";

    //metodo construtor:
    public NotasDAO(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //criando a estrutura do banco de dados
        String sql = "CREATE TABLE "+TABELA+" ( " +
                " "+ID+" INTEGER PRIMARY KEY, "+
                " "+TITULO+" TEXT, "+NOTA+" TEXT);";
        //executando o SQL
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //caso haja atualizacao no banco de dados
        String sql = "DROP TABLE IF EXISTS "+TABELA;
        db.execSQL(sql);
        onCreate(db);
    }

    //salvando novo registro no banco de dados
    public long salvarNota(Nota n){
        ContentValues values = new ContentValues(); //vai armazenar os dados do objeto
        long retornoDB;

        values.put(TITULO, n.getTitulo());
        values.put(NOTA, n.getNota());

        retornoDB = getWritableDatabase().insert(TABELA, null, values); //fazendo o insert
        return retornoDB; //retorna se deu certo ou nao a execucao
    }

    //atualizando registro no banco de dados
    public long atualizarNota(Nota p){
        ContentValues values = new ContentValues(); //vai armazenar os dados do objeto
        long retornoDB;

        values.put(TITULO, p.getTitulo());
        values.put(NOTA, p.getNota());

        String[] argumentos = {String.valueOf(p.getId())}; //coletando o ID em questão
        retornoDB = getWritableDatabase().update(TABELA, values, ID+"=?",argumentos); //fazendo o update onde o id= a alguma coisa
        return retornoDB; //retorna se deu certo ou nao a execucao
    }

    //atualizando registro no banco de dados
    public long removerRegistro(Nota p){
        long retornoDB;

        String[] argumentos = {String.valueOf(p.getId())}; //coletando o ID em questão
        retornoDB = getWritableDatabase().delete(TABELA, ID+"=?",argumentos);
        return retornoDB; //retorna se deu certo ou nao a execucao
    }

    //fazedo select *
    public ArrayList<Nota> selectAllNotas(){
        String[] colunas = {ID, TITULO, NOTA};
        Cursor cursor = getWritableDatabase().query(TABELA, colunas, null,null,null,null,"titulo ASC");

        //array que vai armazenar os registros
        ArrayList<Nota> listNota = new ArrayList<Nota>();

        //enquanto houver registros, adiciona a pessoa a listagem
        while (cursor.moveToNext()){
            Nota nota = new Nota();
            nota.setId(cursor.getInt(0));
            nota.setTitulo(cursor.getString(1));
            nota.setNota(cursor.getString(2));
            //adicionando o elemento completo na listagem:
            listNota.add(nota);
        }
        return listNota; //volta a listagem completa
    }
}