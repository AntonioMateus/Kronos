package br.com.kronos.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.kronos.kronos.Atividade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class KronosDatabase extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Kronos";

    public KronosDatabase (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {
        bd.execSQL(KronosContract.SQL_CREATE_ENTRIES1);
        bd.execSQL(KronosContract.SQL_CREATE_ENTRIES2);
    }

    @Override
    public void onUpgrade (SQLiteDatabase bd, int oldVersion, int newVersion) {
        bd.execSQL(KronosContract.SQL_DELETE_ENTRIES1);
        bd.execSQL(KronosContract.SQL_DELETE_ENTRIES2);
        onCreate(bd);
    }

    public void addAtividadeHistorico(Atividade atividade) {
        SQLiteDatabase bd = this.getWritableDatabase();

        String dataAAdicionar = getDataFormatada(atividade.getDia(), atividade.getMes(), atividade.getAno());

        ContentValues tuplaASerAdicionada = new ContentValues();
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_NOME,atividade.getNome());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_DURACAO,atividade.getDuracao());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_QUALIDADE,atividade.getQualidade());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_DATA, dataAAdicionar);

        bd.insert(KronosContract.FeedEntry.TABLE_HISTORICO_NAME, null, tuplaASerAdicionada);
        bd.close();
    }

    public void addAtividadeLista(Atividade atividade) {
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues tuplaASerAdicionada = new ContentValues();
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_LISTA_NAME_NOME, atividade.getNome());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_LISTA_NAME_DURACAO, atividade.getDuracao());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_LISTA_NAME_QUALIDADE, atividade.getQualidade());

        bd.insert(KronosContract.FeedEntry.TABLE_LISTA_NAME, null, tuplaASerAdicionada);
        bd.close();
    }

    public void removeAtividadeHistorico(Atividade atividade) {
        SQLiteDatabase bd = getWritableDatabase();

        String selecao = KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_NOME + "=? AND " +
                        KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_DATA + "=?";
        String[] selecaoArgs = new String[]{atividade.getNome(), getDataFormatada(atividade.getDia(), atividade.getMes(), atividade.getAno())};
        bd.delete(KronosContract.FeedEntry.TABLE_HISTORICO_NAME, selecao, selecaoArgs);

        bd.close();
    }

    public void removeAtividadeLista(Atividade atividade) {
        SQLiteDatabase bd = this.getWritableDatabase();
        bd.delete(KronosContract.FeedEntry.TABLE_LISTA_NAME, KronosContract.FeedEntry.COLUMN_LISTA_NAME_NOME + "=?",
                new String[]{atividade.getNome()});

        bd.close();
    }

    public List<Atividade> getAtividadesLista() {
        SQLiteDatabase bd = this.getReadableDatabase();
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
        SQLiteDatabase bd = getReadableDatabase();

        String[] projecao = {KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_NOME,
                            KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_DURACAO,
                            KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_QUALIDADE};
        String selecao = KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_DATA + "=?";
        String data = getDataFormatada(dia, mes, ano);
        String[] selecaoArgs = new String[]{data};

        Cursor iteradorTuplas = bd.query(KronosContract.FeedEntry.TABLE_HISTORICO_NAME, projecao, selecao, selecaoArgs,null,null,null,null);

        ArrayList<Atividade> atividadesARetornar = new ArrayList<>();
        while (iteradorTuplas.moveToNext()) {
            String nome = iteradorTuplas.getString(0);
            double duracao = iteradorTuplas.getDouble(1);
            int qualidade = iteradorTuplas.getInt(2);
            Atividade atividadeReferenciada = new Atividade(nome, duracao, qualidade, dia, mes, ano );
            atividadesARetornar.add(atividadeReferenciada);
        }

        iteradorTuplas.close();
        bd.close();
        return atividadesARetornar;
    }

    public void updateLista (Atividade a, String nomeAntigo) {
        SQLiteDatabase bd = this.getReadableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(KronosContract.FeedEntry.COLUMN_LISTA_NAME_NOME, a.getNome());
        valores.put(KronosContract.FeedEntry.COLUMN_LISTA_NAME_DURACAO, a.getDuracao());
        valores.put(KronosContract.FeedEntry.COLUMN_LISTA_NAME_QUALIDADE, a.getQualidade());

        String selecao = KronosContract.FeedEntry.COLUMN_LISTA_NAME_NOME + " LIKE ?";
        String[] valoresSelecao = {nomeAntigo};
        int count = bd.update(
            KronosContract.FeedEntry.TABLE_LISTA_NAME,
            valores,
            selecao,
            valoresSelecao
        );
    }

    public void updateHistorico (Atividade a, String nomeAntigo) {
        SQLiteDatabase bd = this.getReadableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_NOME, a.getNome());
        valores.put(KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_DURACAO, a.getDuracao());
        valores.put(KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_DATA, a.getQualidade());
        valores.put(KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_QUALIDADE, a.getQualidade());

        String selecao = KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_NOME +" LIKE ?";
        String[] valoresSelecao = {nomeAntigo};
        bd.update ( KronosContract.FeedEntry.TABLE_HISTORICO_NAME, valores, selecao, valoresSelecao);
    }

    private String getDataFormatada(int dia, int mes, int ano) {
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
        return diaBD +"_" +mesBD +"_" +ano;
    }
}
