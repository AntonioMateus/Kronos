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

    public void addAtividadeHistorico(Atividade a) {
        SQLiteDatabase bd = dbHelper.getWritableDatabase();
        ContentValues tuplaASerAdicionada = new ContentValues();
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN1_NAME_NOME,a.getNome());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN1_NAME_DURACAO,a.getDuracao());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN1_NAME_QUALIDADE,a.getQualidade());

        String dataAAdicionar, diaBD, mesBD;
        if (a.getDia() < 10)
            diaBD = "0" +Integer.toString(a.getDia());
        else
            diaBD = Integer.toString(a.getDia());

        if (a.getMes() < 10)
            mesBD = "0" +Integer.toString(a.getMes());
        else
            mesBD = Integer.toString(a.getMes());

        dataAAdicionar = diaBD +"_" +mesBD +"_" +a.getAno();
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN1_NAME_DATA,dataAAdicionar);

        bd.insert(KronosContract.FeedEntry.TABLE1_NAME, null, tuplaASerAdicionada);
    }

    public void addAtividadeLista(Atividade a) {
        SQLiteDatabase bd = dbHelper.getWritableDatabase();
        ContentValues tuplaASerAdicionada = new ContentValues();
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN2_NAME_NOME, a.getNome());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN2_NAME_DURACAO,a.getDuracao());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN2_NAME_QUALIDADE,a.getQualidade());

        bd.insert(KronosContract.FeedEntry.TABLE2_NAME, null, tuplaASerAdicionada);
    }

    public void removeAtividadeHistorico(Atividade a) {
        SQLiteDatabase bd = dbHelper.getWritableDatabase();
        bd.delete(KronosContract.FeedEntry.TABLE1_NAME, KronosContract.FeedEntry.COLUMN1_NAME_NOME + "=? AND "
                + KronosContract.FeedEntry.COLUMN1_NAME_DURACAO + "=?", new String[]{a.getNome(), Double.toString(a.getDuracao())});
    }

    public void removeAtividadeLista(Atividade a) {
        SQLiteDatabase bd = dbHelper.getWritableDatabase();
        bd.delete(KronosContract.FeedEntry.TABLE2_NAME, KronosContract.FeedEntry.COLUMN2_NAME_NOME + "=? AND "
                + KronosContract.FeedEntry.COLUMN2_NAME_DURACAO + "=?", new String[]{a.getNome(), Double.toString(a.getDuracao())});
    }

    public List<Atividade> getAtividadesLista() {
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
        Cursor iteradorTuplas = bd.query(KronosContract.FeedEntry.TABLE1_NAME, null, KronosContract.FeedEntry.COLUMN1_NAME_DATA+"=?",new String[]{data},null,null,null,null);
        ArrayList<Atividade> atividadesARetornar = new ArrayList<>();
        if (iteradorTuplas.getCount() > 0) {
            iteradorTuplas.moveToFirst();
            while (!iteradorTuplas.isAfterLast()) {
                Atividade atividadeReferenciada = new Atividade(iteradorTuplas.getString(0),iteradorTuplas.getDouble(1),iteradorTuplas.getInt(2),dia,mes,ano);
                atividadesARetornar.add(atividadeReferenciada);
                iteradorTuplas.moveToNext();
            }
        }
        return atividadesARetornar;
    }

    /*
    public List<Atividade> getAtividadesLista (String data) {
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
    */
}