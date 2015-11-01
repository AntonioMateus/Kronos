package br.com.kronos.kronos;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by antonio on 31/10/15.
 */
public class KronosDatabase {
    final KronosDbHelper dbHelper;

    public KronosDatabase(Context context) {
        dbHelper = new KronosDbHelper(context);
    }

    public void insereAtividadeNoHistorico (Atividade a) {
        SQLiteDatabase bd = dbHelper.getWritableDatabase();
        ContentValues tuplaASerAdicionada = new ContentValues();
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN1_NAME_NOME,a.getNome());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN1_NAME_DURACAO,a.getDuracao());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN1_NAME_QUALIDADE,a.getQualidade());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN1_NAME_DATA,a.getData());

        bd.insert(KronosContract.FeedEntry.TABLE1_NAME,null,tuplaASerAdicionada);
    }

    public void insereAtividadeNaLista (Atividade a) {
        SQLiteDatabase bd = dbHelper.getWritableDatabase();
        ContentValues tuplaASerAdicionada = new ContentValues();
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN2_NAME_NOME, a.getNome());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN2_NAME_DURACAO,a.getDuracao());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN2_NAME_QUALIDADE,a.getQualidade());

        bd.insert(KronosContract.FeedEntry.TABLE2_NAME, null, tuplaASerAdicionada);
    }

    public void removeAtividadeDoHistorico (Atividade a) {
        SQLiteDatabase bd = dbHelper.getWritableDatabase();
        bd.delete(KronosContract.FeedEntry.TABLE1_NAME, KronosContract.FeedEntry.COLUMN1_NAME_NOME + "=? AND "
                + KronosContract.FeedEntry.COLUMN1_NAME_DURACAO + "=?", new String[]{a.getNome(), Double.toString(a.getDuracao())});
    }

    public void removeAtividadeDaLista (Atividade a) {
        SQLiteDatabase bd = dbHelper.getWritableDatabase();
        bd.delete(KronosContract.FeedEntry.TABLE2_NAME, KronosContract.FeedEntry.COLUMN2_NAME_NOME + "=? AND "
                + KronosContract.FeedEntry.COLUMN2_NAME_DURACAO + "=?", new String[]{a.getNome(), Double.toString(a.getDuracao())});
    }

    public List<Atividade> retornaAtividadesLista () {
        SQLiteDatabase bd = dbHelper.getReadableDatabase();
        Cursor iteradorTuplas = bd.query(KronosContract.FeedEntry.TABLE2_NAME, null, null, null, null, null, null, null);
        ArrayList<Atividade> atividadesARetornar = new ArrayList<>();
        if (iteradorTuplas.getCount() > 0) {
            iteradorTuplas.moveToFirst();
            while (!iteradorTuplas.isAfterLast()) {
                Atividade atividadeReferenciada = new Atividade(iteradorTuplas.getString(0),iteradorTuplas.getDouble(1),iteradorTuplas.getInt(2), Calendar.DAY_OF_MONTH, Calendar.MONTH, Calendar.YEAR);
                atividadesARetornar.add(atividadeReferenciada);
                iteradorTuplas.moveToNext();
            }
        }
        return atividadesARetornar;
    }

    public List<Atividade> retornaAtividadesHistoricoDataEspecifica (String data) {
        SQLiteDatabase bd = dbHelper.getReadableDatabase();
        Cursor iteradorTuplas = bd.query(KronosContract.FeedEntry.TABLE1_NAME, null, KronosContract.FeedEntry.COLUMN1_NAME_DATA+"=?",new String[]{data},null,null,null,null);
        ArrayList<Atividade> atividadesARetornar = new ArrayList<>();
        if (iteradorTuplas.getCount() > 0) {
            iteradorTuplas.moveToFirst();
            while (!iteradorTuplas.isAfterLast()) {
                Atividade atividadeReferenciada = new Atividade(iteradorTuplas.getString(0),iteradorTuplas.getDouble(1),iteradorTuplas.getInt(2),iteradorTuplas.getString(3));
                atividadesARetornar.add(atividadeReferenciada);
                iteradorTuplas.moveToNext();
            }
        }
        return atividadesARetornar;
    }

    public List<Atividade> retornaAtividadesListaDataEspecifica (String data) {
        SQLiteDatabase bd = dbHelper.getReadableDatabase();
        Cursor iteradorTuplas = bd.query(KronosContract.FeedEntry.TABLE2_NAME, null, null, null, null, null,null,null);
        ArrayList<Atividade> atividadesARetornar = new ArrayList<>();
        if (iteradorTuplas.getCount() > 0) {
            iteradorTuplas.moveToFirst();
            while (!iteradorTuplas.isAfterLast()) {
                Atividade atividadeReferenciada = new Atividade(iteradorTuplas.getString(0),iteradorTuplas.getDouble(1),iteradorTuplas.getInt(2),data);
                atividadesARetornar.add(atividadeReferenciada);
                iteradorTuplas.moveToNext();
            }
        }
        return atividadesARetornar;
    }
}