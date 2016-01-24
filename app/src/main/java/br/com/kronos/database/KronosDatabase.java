package br.com.kronos.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import br.com.kronos.exceptions.DescricaoDeMetaInvalidaException;
import br.com.kronos.exceptions.MetaRepetidaException;
import br.com.kronos.exceptions.TempoEstipuladoInvalidoException;
import br.com.kronos.kronos.Atividade;
import br.com.kronos.kronos.Meta;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class KronosDatabase extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
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
        bd.execSQL(KronosContract.SQL_CREATE_ENTRIES5);
    }

    @Override
    public void onUpgrade (SQLiteDatabase bd, int oldVersion, int newVersion) {
        bd.execSQL(KronosContract.SQL_DELETE_ENTRIES1);
        bd.execSQL(KronosContract.SQL_DELETE_ENTRIES2);
        bd.execSQL(KronosContract.SQL_DELETE_ENTRIES3);
        bd.execSQL(KronosContract.SQL_DELETE_ENTRIES4);
        bd.execSQL(KronosContract.SQL_DELETE_ENTRIES5);
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

    public void addMeta (Meta meta) throws MetaRepetidaException {
        SQLiteDatabase bd = getWritableDatabase();
        ContentValues tuplaASerAdicionada = new ContentValues();
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_META_NAME_DESCRICAO, meta.getDescricao());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_META_NAME_PRAZO, meta.getPrazo());
        int repetir = 0;
        if (meta.getRepetir()){
            repetir = 1;
        }
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_META_NAME_REPETIR, repetir);
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_META_NAME_CATEGORIA, meta.getCategoria());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_META_NAME_DATA_INICIO, getDataFormatada(meta.getDiaInicio(), meta.getMesInicio(), meta.getAnoInicio()));
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_META_NAME_TEMPO_ACUMULADO, meta.getTempoAcumulado());
        tuplaASerAdicionada.put(KronosContract.FeedEntry.COLUMN_META_NAME_TEMPO_ESTIPULADO, meta.getTempoEstipulado());

        long insert = bd.insert(KronosContract.FeedEntry.TABLE_META_NAME, null, tuplaASerAdicionada);
        if(insert == -1) { //código de erro padrao quando algum erro aconteceu
            // muito provavelmente aconteceu de a Meta ter o mesmo nome que outra
            throw new MetaRepetidaException();
        }
        bd.close();

        if (meta.getMetaAtingida()) {
            addMetaCumprida(meta);
        }
        associarAtividadesAMeta(meta);
    }

    private void associarAtividadesAMeta(Meta meta) {
        SQLiteDatabase writableDatabase = this.getWritableDatabase();

        List<Atividade> atividadesAssociadas = meta.getAtividadesAssociadas();
        for (Atividade atividade : atividadesAssociadas) {
            ContentValues tuplaAInserir = new ContentValues();
            tuplaAInserir.put(KronosContract.FeedEntry.COLUMN_META_ASSOCIADA_ATIVIDADE_NAME_ATIVIDADE_NOME, atividade.getNome());
            tuplaAInserir.put(KronosContract.FeedEntry.COLUMN_META_ASSOCIADA_ATIVIDADE_NAME_META_DESCRICAO, meta.getDescricao());

            writableDatabase.insert(KronosContract.FeedEntry.TABLE_META_ASSOCIADA_ATIVIDADE_NAME, null, tuplaAInserir);
        }

        writableDatabase.close();
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
        iteradorTuplas.close();
        bd.close();
        return (int) Math.round((tempoAcumulado/tempoEstipulado)*100);
    }

    public List<Meta> getMetas() {
        SQLiteDatabase leitor = getReadableDatabase();

        String[] projecao = {KronosContract.FeedEntry.COLUMN_META_NAME_DESCRICAO,
                KronosContract.FeedEntry.COLUMN_META_NAME_PRAZO,
                KronosContract.FeedEntry.COLUMN_META_NAME_REPETIR,
                KronosContract.FeedEntry.COLUMN_META_NAME_CATEGORIA,
                KronosContract.FeedEntry.COLUMN_META_NAME_DATA_INICIO,
                KronosContract.FeedEntry.COLUMN_META_NAME_TEMPO_ESTIPULADO,
                KronosContract.FeedEntry.COLUMN_META_NAME_TEMPO_ACUMULADO};
        Cursor cursor = leitor.query(KronosContract.FeedEntry.TABLE_META_NAME, projecao, null, null, null, null, null);

        List<Meta> metas = new LinkedList<>();
        while (cursor.moveToNext()) {
            String descricao = cursor.getString(0);
            double prazo = cursor.getDouble(1);
            boolean repetir = cursor.getInt(2) == 1;
            String categoria = cursor.getString(3);
            String[] dataInicio = cursor.getString(4).split("_");
            int diaInicio = Integer.valueOf(dataInicio[0]);
            int mesInicio = Integer.valueOf(dataInicio[1]);
            int anoInicio = Integer.valueOf(dataInicio[2]);
            try {
                Meta meta = new Meta(descricao, prazo, repetir, categoria, diaInicio, mesInicio, anoInicio);

                double tempoEstipulado = cursor.getDouble(5);
                meta.setTempoEstipulado(tempoEstipulado);
                double tempoAcumulado = cursor.getDouble(6);
                meta.setTempoAcumulado(tempoAcumulado);

                metas.add(meta);
            } catch (DescricaoDeMetaInvalidaException | TempoEstipuladoInvalidoException metaException) {
                //caso improvavel que só acontencerá caso tiver tido uma inserção incorreta no banco de dados
                metaException.printStackTrace();
            }
        }

        cursor.close();
        leitor.close();
        return metas;
    }

    public Meta devolveMeta (String descricao) {
        SQLiteDatabase bd = getReadableDatabase();
        String[] projecao = {KronosContract.FeedEntry.COLUMN_META_NAME_DESCRICAO,
                KronosContract.FeedEntry.COLUMN_META_NAME_CATEGORIA,
                KronosContract.FeedEntry.COLUMN_META_NAME_TEMPO_ACUMULADO,
                KronosContract.FeedEntry.COLUMN_META_NAME_TEMPO_ESTIPULADO,
                KronosContract.FeedEntry.COLUMN_META_NAME_DATA_INICIO,
                KronosContract.FeedEntry.COLUMN_META_NAME_PRAZO,
                KronosContract.FeedEntry.COLUMN_META_NAME_REPETIR};
        String selecao = KronosContract.FeedEntry.COLUMN_META_NAME_DESCRICAO + "=?";
        String[] selecaoArgs = {descricao};
        Cursor iteradorTuplas = bd.query(KronosContract.FeedEntry.TABLE_META_NAME, projecao, selecao, selecaoArgs,null,null,null,null);

        Meta metaRetornada = null;
        if (iteradorTuplas.getCount() > 0) {
            iteradorTuplas.moveToFirst();
            String[] dataInicio = iteradorTuplas.getString(4).split("_");
            double prazo = iteradorTuplas.getInt(1);
            boolean repetir = (iteradorTuplas.getInt(5) == 1);
            String categoria = iteradorTuplas.getString(6);
            int diaInicio = Integer.valueOf(dataInicio[0]);
            int mesInicio = Integer.valueOf(dataInicio[1]);
            int anoInicio = Integer.valueOf(dataInicio[2]);
            try {
                metaRetornada = new Meta(descricao, prazo, repetir, categoria, diaInicio, mesInicio, anoInicio);
                metaRetornada.setTempoAcumulado(iteradorTuplas.getDouble(2));
                metaRetornada.setTempoEstipulado(iteradorTuplas.getDouble(3));
            } catch (DescricaoDeMetaInvalidaException | TempoEstipuladoInvalidoException e) {
                //Aconteceu alguma coisa errada no momento de adicionar as Metas
                e.printStackTrace();
            }
        }
        iteradorTuplas.close();
        bd.close();

        return metaRetornada;
    }


    public void removeMeta (Meta meta) {
        SQLiteDatabase bd = this.getWritableDatabase();

        String selecao = KronosContract.FeedEntry.COLUMN_META_NAME_DESCRICAO + "=?";
        String[] selecaoArgs = new String[]{meta.getDescricao()};
        bd.delete(KronosContract.FeedEntry.TABLE_META_NAME, selecao, selecaoArgs);
        if (meta.getMetaAtingida()) {
            bd.delete(KronosContract.FeedEntry.TABLE_META_CUMPRIDA_NAME, selecao, selecaoArgs);
        }
        bd.close();
    }

    public List<String> getCategorias() {
        SQLiteDatabase bd = this.getReadableDatabase();
        String[] projecao = {KronosContract.FeedEntry.COLUMN_META_NAME_CATEGORIA};
        Cursor iteradorTuplas = bd.query(KronosContract.FeedEntry.TABLE_META_NAME, projecao, null, null, KronosContract.FeedEntry.COLUMN_META_NAME_CATEGORIA,null,null);

        ArrayList<String> categoriasEncontradas = new ArrayList<>();
        if (iteradorTuplas.getCount() > 0) {
            iteradorTuplas.moveToFirst();
            while (!iteradorTuplas.isAfterLast()) {
                String categoria = iteradorTuplas.getString(0);
                categoriasEncontradas.add(categoria);
                iteradorTuplas.moveToNext();
            }
        }
        iteradorTuplas.close();
        bd.close();
        return categoriasEncontradas;
    }

    public List<Meta> devolveMetasCategoria (String categoria) {
        SQLiteDatabase bd = getReadableDatabase();
        String[] projecao = {KronosContract.FeedEntry.COLUMN_META_NAME_DESCRICAO};
        String selecao = KronosContract.FeedEntry.COLUMN_META_NAME_CATEGORIA+"=?";
        String[] selecaoArgs = {categoria};
        List<Meta> metasCategoria = new LinkedList<>();
        Cursor iterador = bd.query(KronosContract.FeedEntry.TABLE_META_NAME,projecao,selecao,selecaoArgs,null,null,null);
        if (iterador.getCount() > 0) {
            iterador.moveToFirst();
            while (!iterador.isAfterLast()) {
                metasCategoria.add(devolveMeta(iterador.getString(0)));
                iterador.moveToNext();
            }
        }
        iterador.close();
        bd.close();
        return metasCategoria;
    }

    /*
    public HashMap<String, List<Meta>> devolveRelacaoCategoriaMeta() {
        HashMap<String, List<Meta>> mapa = new HashMap<>();
        List<String> categorias = getCategorias();
        for (String cat : categorias) {
            mapa.put(cat,devolveMetasCategoria(cat));
        }
        return mapa;
    }
    */

    public void removeTodasMetas() {
        SQLiteDatabase bd = this.getWritableDatabase();
        bd.delete(KronosContract.FeedEntry.TABLE_META_CUMPRIDA_NAME, null, null);
        bd.delete(KronosContract.FeedEntry.TABLE_META_NAME, null, null);
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


    public List<Atividade> getAtividadesDistintas() {
        SQLiteDatabase bd = getReadableDatabase();

        String[] projecao = {KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_NOME};
        String groupBy = KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_NOME;
        Cursor iteradorTuplas = bd.query(KronosContract.FeedEntry.TABLE_HISTORICO_NAME, projecao, null, null, groupBy, null, null, null);

        ArrayList<Atividade> atividadesARetornar = new ArrayList<>();
        while (iteradorTuplas.moveToNext()) {
            String nome = iteradorTuplas.getString(0);
            Atividade atividadeReferenciada = new Atividade(nome, Atividade.DURACAO_MINIMA, 0, 1, 1, 0 );
            atividadesARetornar.add(atividadeReferenciada);
        }

        iteradorTuplas.close();
        bd.close();
        return atividadesARetornar;
    }

    public List<Meta> getMetasComTempoAcumulado() {
        SQLiteDatabase leitor = getWritableDatabase();
        String sql = "SELECT " + KronosContract.FeedEntry.COLUMN_META_ASSOCIADA_ATIVIDADE_NAME_META_DESCRICAO + ", " +
                KronosContract.FeedEntry.COLUMN_META_NAME_TEMPO_ESTIPULADO + ", " +
                KronosContract.FeedEntry.COLUMN_META_NAME_DATA_INICIO + ", " +
                KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_DATA + ", " +
                KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_DURACAO +
                " FROM " + KronosContract.FeedEntry.TABLE_META_ASSOCIADA_ATIVIDADE_NAME + ", " +
                KronosContract.FeedEntry.TABLE_HISTORICO_NAME + ", " +
                KronosContract.FeedEntry.TABLE_META_NAME +
                " WHERE " + KronosContract.FeedEntry.COLUMN_META_ASSOCIADA_ATIVIDADE_NAME_ATIVIDADE_NOME + " = " +
                KronosContract.FeedEntry.COLUMN_HISTORICO_NAME_NOME + " AND " +
                KronosContract.FeedEntry.COLUMN_META_ASSOCIADA_ATIVIDADE_NAME_META_DESCRICAO + " = " +
                KronosContract.FeedEntry.COLUMN_META_NAME_DESCRICAO;
        Cursor cursor = leitor.rawQuery(sql, null);

        Map<String, Meta> metas = new HashMap<>();
        //TODO - conferir se a data da atividade esta dentro do prazo de avaliacao da Meta
        while (cursor.moveToNext()) {
            String metaDescricao = cursor.getString(0);

            Meta meta = metas.get(metaDescricao);
            if (meta == null) {
                double tempoEstipulado = cursor.getDouble(1);
                String[] data = cursor.getString(2).split("_");
                int diaInicio = Integer.parseInt(data[0]);
                int mesInicio = Integer.parseInt(data[1]);
                int anoInicio = Integer.parseInt(data[2]);
                try {
                    meta = new Meta(metaDescricao, tempoEstipulado, diaInicio, mesInicio, anoInicio);
                } catch (DescricaoDeMetaInvalidaException | TempoEstipuladoInvalidoException e) {
                    e.printStackTrace();
                }
            }

            double atividadeDuracao = cursor.getDouble(4);
            assert meta != null; //indica que a meta nunca pode ser nula ao chegar a esse ponto
            meta.setTempoAcumulado(meta.getTempoEstipulado() + atividadeDuracao);

        }

        cursor.close();
        leitor.close();

        List<Meta> listaDeMetas = new LinkedList<>();
        for (String metaDescricao : metas.keySet()) {
            Meta meta = metas.get(metaDescricao);
            listaDeMetas.add(meta);
        }

        return listaDeMetas;
    }
}


