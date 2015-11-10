package br.com.kronos.kronos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class KronosDatabase {
    final KronosDbHelper dbHelper;

    public KronosDatabase(Context context) {
        dbHelper = new KronosDbHelper(context);
    }

    public void addAtividadeHistorico(Atividade atividade) {

        SQLiteDatabase bd = dbHelper.getWritableDatabase();

        ContentValues tuplaASerAdicionada = new ContentValues();
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_NOME,atividade.getNome());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_DURACAO,atividade.getDuracao());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_QUALIDADE,atividade.getQualidade());

        String dataAAdicionar, diaBD, mesBD;
        if (atividade.getDia() < 10)
            diaBD = "0" +Integer.toString(atividade.getDia());
        else
            diaBD = Integer.toString(atividade.getDia());

        if (atividade.getMes() < 10)
            mesBD = "0" +Integer.toString(atividade.getMes());
        else
            mesBD = Integer.toString(atividade.getMes());

        dataAAdicionar = diaBD +"_" +mesBD +"_" +atividade.getAno();
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_DATA,dataAAdicionar);

        bd.insert(KronosContract.FeedEntry.TABLE_HISTORICO_NAME, null, tuplaASerAdicionada);
        bd.close();
    }

    public void addAtividadeLista(Atividade atividade) {
        SQLiteDatabase bd = dbHelper.getWritableDatabase();
        ContentValues tuplaASerAdicionada = new ContentValues();
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_LISTA_NAME_NOME, atividade.getNome());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_LISTA_NAME_DURACAO, atividade.getDuracao());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_LISTA_NAME_QUALIDADE, atividade.getQualidade());

        bd.insert(KronosContract.FeedEntry.TABLE_LISTA_NAME, null, tuplaASerAdicionada);
        bd.close();

    }

    public void removeAtividadeHistorico(Atividade atividade) {
        SQLiteDatabase bd = dbHelper.getWritableDatabase();
        bd.delete(KronosContract.FeedEntry.TABLE_HISTORICO_NAME, KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_NOME + "=?",
                new String[]{atividade.getNome()});

        bd.close();
    }

    public void removeAtividadeLista(Atividade atividade) {
        SQLiteDatabase bd = dbHelper.getWritableDatabase();
        bd.delete(KronosContract.FeedEntry.TABLE_LISTA_NAME, KronosContract.FeedEntry.COLUMN_LISTA_NAME_NOME + "=?",
                new String[]{atividade.getNome()});

        bd.close();
    }

    public List<Atividade> getAtividadesLista() {
        SQLiteDatabase bd = dbHelper.getReadableDatabase();
        Cursor iteradorTuplas = bd.query(KronosContract.FeedEntry.TABLE_LISTA_NAME, null, null, null, null, null, null, null);
        ArrayList<Atividade> atividadesARetornar = new ArrayList<>();
        if (iteradorTuplas.getCount() > 0) {
            iteradorTuplas.moveToFirst();
            while (!iteradorTuplas.isAfterLast()) {
                Atividade atividadeReferenciada = new Atividade(iteradorTuplas.getString(0),iteradorTuplas.getDouble(1),iteradorTuplas.getInt(2), Calendar.DAY_OF_MONTH, Calendar.MONTH, Calendar.YEAR);
                atividadesARetornar.add(atividadeReferenciada);
                iteradorTuplas.moveToNext();
            }
        }

        iteradorTuplas.close();
        bd.close();
        return atividadesARetornar;
    }

    public List<Atividade> getAtividadesHistorico(int dia, int mes, int ano) {
        String diaBD;
        if (dia < 10)
            diaBD = "0" +Integer.toString(dia);
        else
            diaBD = Integer.toString(dia);
        String mesBD;
        if (mes < 10)
            mesBD = "0" +Integer.toString(mes);
        else
            mesBD = Integer.toString(mes);
        String data = diaBD +"_" +mesBD +"_" +ano;

        SQLiteDatabase bd = dbHelper.getReadableDatabase();
        Cursor iteradorTuplas = bd.query(KronosContract.FeedEntry.TABLE_HISTORICO_NAME, null, KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_DATA+"=?",new String[]{data},null,null,null,null);
        ArrayList<Atividade> atividadesARetornar = new ArrayList<>();
        if (iteradorTuplas.getCount() > 0) {
            iteradorTuplas.moveToFirst();
            while (!iteradorTuplas.isAfterLast()) {
                Atividade atividadeReferenciada = new Atividade(iteradorTuplas.getString(0),iteradorTuplas.getDouble(1),iteradorTuplas.getInt(2),dia,mes,ano);
                atividadesARetornar.add(atividadeReferenciada);
                iteradorTuplas.moveToNext();
            }
        }

        iteradorTuplas.close();
        bd.close();
        return atividadesARetornar;
    }
}