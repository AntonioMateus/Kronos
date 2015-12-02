package br.com.kronos.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.kronos.kronos.Atividade;
import br.com.kronos.kronos.Meta;

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
        bd.execSQL(KronosContract.SQL_CREATE_ENTRIES3);
        bd.execSQL(KronosContract.SQL_CREATE_ENTRIES4);
    }

    @Override
    public void onUpgrade (SQLiteDatabase bd, int oldVersion, int newVersion) {
        bd.execSQL(KronosContract.SQL_DELETE_ENTRIES1);
        bd.execSQL(KronosContract.SQL_DELETE_ENTRIES2);
        bd.execSQL(KronosContract.SQL_DELETE_ENTRIES3);
        bd.execSQL(KronosContract.SQL_DELETE_ENTRIES4);
        onCreate(bd);
    }

    public void addAtividadeHistorico(Atividade atividade) {
        SQLiteDatabase bd = this.getWritableDatabase();

        String dataAAdicionar = getDataFormatada(atividade.getDia(), atividade.getMes(), atividade.getAno());

        ContentValues tuplaASerAdicionada = new ContentValues();
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_NOME,atividade.getNome());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_DURACAO, atividade.getDuracao());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_QUALIDADE, atividade.getQualidade());
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

    public void addMeta (Meta m) {
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues tuplaASerAdicionada = new ContentValues();
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_META_NAME_DESCRICAO,m.getDescricao());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_META_NAME_PRAZO,m.getPrazo());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_META_NAME_REPETIR,m.getRepetir());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_META_NAME_CATEGORIA,m.getCategoria());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_META_NAME_DATA_INICIO,getDataFormatada(m.getDiaInicio(), m.getMesInicio(), m.getAnoInicio()));
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_META_NAME_TEMPO_ACUMULADO,m.getTempoAcumulado());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_META_NAME_TEMPO_ESTIPULADO,m.getTempoEstipulado());

        bd.insert(KronosContract.FeedEntry.TABLE_META_NAME, null, tuplaASerAdicionada);
        if (m.getMetaTerminada()) {
            tuplaASerAdicionada = new ContentValues();
            tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_META_CUMPRIDA_NAME_DATA_CUMPRIDA,getDataFormatada(m.getDiaTermino(),m.getMesTermino(),m.getAnoTermino()));
            tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_META_CUMPRIDA_NAME_DESCRICAO_META, m.getDescricao());
            tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_META_CUMPRIDA_NAME_TEMPO_EXCEDIDO, m.getTempoExcedido());
            bd.insert(KronosContract.FeedEntry.TABLE_META_CUMPRIDA_NAME,null,tuplaASerAdicionada);
        }
        bd.close();
    }

    public void addMetaCumprida(Meta meta) {
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues tupla = new ContentValues();
        tupla.put(KronosContract.FeedEntry.COLUMN_META_CUMPRIDA_NAME_DESCRICAO_META, meta.getDescricao());
        tupla.put(KronosContract.FeedEntry.COLUMN_META_CUMPRIDA_NAME_TEMPO_EXCEDIDO, meta.getTempoExcedido());
        tupla.put(KronosContract.FeedEntry.COLUMN_META_CUMPRIDA_NAME_DATA_CUMPRIDA, getDataFormatada(meta.getDiaTermino(), meta.getMesTermino(), meta.getAnoTermino()));
        bd.insert(KronosContract.FeedEntry.TABLE_META_CUMPRIDA_NAME, null, tupla);
        bd.close();
    }

    public void updateTempoAcumuladoMeta (String descricaoMeta, double tempoAcumuladoAtualizado) {
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(KronosContract.FeedEntry.COLUMN_META_NAME_TEMPO_ACUMULADO,tempoAcumuladoAtualizado);

        String selecao = KronosContract.FeedEntry.COLUMN_META_NAME_DESCRICAO +" LIKE ?";
        String[] valoresSelecao = {descricaoMeta};
        bd.update(KronosContract.FeedEntry.TABLE_HISTORICO_NAME, valores, selecao, valoresSelecao);
        bd.close();
    }

    public int devolveProgressoMeta (String descricaoMeta){
        SQLiteDatabase bd = this.getReadableDatabase();
        String[] projecao = {KronosContract.FeedEntry.COLUMN_META_NAME_TEMPO_ESTIPULADO,
                            KronosContract.FeedEntry.COLUMN_META_NAME_TEMPO_ACUMULADO};
        String selecao = KronosContract.FeedEntry.COLUMN_META_NAME_DESCRICAO +"=?";
        String[] selecaoArgs = {descricaoMeta};
        double tempoAcumulado = 1.0;
        double tempoEstipulado = 1.0;
        Cursor iteradorTuplas = bd.query(KronosContract.FeedEntry.TABLE_META_NAME,projecao,selecao,selecaoArgs,null,null,null,null);
        if (iteradorTuplas.getCount() > 0) {
            iteradorTuplas.moveToFirst();
            while (!iteradorTuplas.isAfterLast()) {
                tempoEstipulado = iteradorTuplas.getDouble(0);
                tempoAcumulado = iteradorTuplas.getDouble(1);
                iteradorTuplas.moveToNext();
            }
        }
        return (int) Math.round((tempoAcumulado/tempoEstipulado)*100);
    }

    public void removeMeta (Meta meta) {
        SQLiteDatabase bd = this.getWritableDatabase();

        String selecao = KronosContract.FeedEntry.COLUMN_META_NAME_DESCRICAO + "=?";
        String[] selecaoArgs = new String[]{meta.getDescricao()};
        bd.delete(KronosContract.FeedEntry.TABLE_META_NAME, selecao, selecaoArgs);
        if (meta.getMetaTerminada()) {
            bd.delete(KronosContract.FeedEntry.TABLE_META_CUMPRIDA_NAME,selecao,selecaoArgs);
        }
        bd.close();
    }

    public List<String> getCategorias() {
        SQLiteDatabase bd = this.getReadableDatabase();
        String[] projecao = {KronosContract.FeedEntry.COLUMN_META_NAME_CATEGORIA};
        Cursor iteradorTuplas = bd.query(KronosContract.FeedEntry.TABLE_META_NAME, projecao, null, null, null, null, null, null);

        ArrayList<String> categoriasEncontradas = new ArrayList<>();
        if (iteradorTuplas.getCount() > 0) {
            iteradorTuplas.moveToFirst();
            while (!iteradorTuplas.isAfterLast()) {
                String categoria = iteradorTuplas.getString(0);
                categoriasEncontradas.add(categoria);
                iteradorTuplas.moveToNext();
            }
        }

        return categoriasEncontradas;
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

        Calendar calendario = Calendar.getInstance();
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH) + 1;
        int ano = calendario.get(Calendar.YEAR);

        if (iteradorTuplas.getCount() > 0) {
            iteradorTuplas.moveToFirst();
            while (!iteradorTuplas.isAfterLast()) {
                Atividade atividadeReferenciada = new Atividade(iteradorTuplas.getString(0),iteradorTuplas.getDouble(1),iteradorTuplas.getInt(2), dia, mes, ano);
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
        String[] selecaoArgs = {data};

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
        String data = getDataFormatada(a.getDia(), a.getMes(), a.getAno());
        valores.put(KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_DATA, data);
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


