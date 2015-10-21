package br.com.kronos.kronos;

import android.provider.BaseColumns;

/**
 * Created by antonio on 21/10/15.
 */
public class KronosContract {
    public KronosContract() {}

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE1_NAME = "Meta";
        public static final String COLUMN1_NAME_DESCRICAO = "descricao";
        public static final String COLUMN1_NAME_PRAZO = "prazo";
        public static final String COLUMN1_NAME_TEMPO_ACUMULADO = "tempoAcumulado";
        public static final String COLUMN1_NAME_TEMPO_ESTIPULADO = "tempoEstipulado";
        public static final String COLUMN1_NAME_DATA_INICIO = "dataInicio";
        public static final String COLUMN1_NAME_REPETIR = "repetir";
        public static final String COLUMN1_NAME_CATEGORIA = "categoria";

        public static final String TABLE2_NAME = "MetaCumprida";
        public static final String COLUMN2_NAME_DATA_CUMPRIDA = "dataCumprida";
        public static final String COLUMN2_NAME_TEMPO_EXCEDIDO = "tempoExcedido";
        public static final String COLUMN2_NAME_DESCRICAO_META = "descricaoDaMeta";

        public static final String TABLE3_NAME = "MetaAssociada";
        public static final String COLUMN3_NAME_DESCRICAO_META = "descricaoDaMeta";
        public static final String COLUMN3_NAME_NOME_ATIVIDADE = "nomeDaAtividade";

        public static final String TABLE4_NAME = "HistoricoDeAtividades";
        public static final String COLUMN4_NAME_NOME = "nome";
        public static final String COLUMN4_NAME_DURACAO = "duracao";
        public static final String COLUMN4_NAME_QUALIDADE = "qualidade";
        public static final String COLUMN4_NAME_DATA = "data";
    }

    public static final String TEXT_TYPE = "TEXT";

    public static final String SQL_CREATE_ENTRIES1 =
            "CREATE TABLE " +FeedEntry.TABLE1_NAME + " (" +
            FeedEntry.COLUMN1_NAME_DESCRICAO +", " +FeedEntry.COLUMN1_NAME_PRAZO +", "
            +FeedEntry.COLUMN1_NAME_TEMPO_ACUMULADO +", " +FeedEntry.COLUMN1_NAME_TEMPO_ESTIPULADO +
            ", " +FeedEntry.COLUMN1_NAME_DATA_INICIO + ", " +FeedEntry.COLUMN1_NAME_REPETIR +
            ", " +FeedEntry.COLUMN1_NAME_CATEGORIA + " )";
    public static final String SQL_DELETE_ENTRIES1 = "DROP TABLE IF EXISTS " +FeedEntry.TABLE1_NAME;

    public static final String SQL_CREATE_ENTRIES2 =
            "CREATE TABLE " +FeedEntry.TABLE2_NAME + " (" +
            FeedEntry.COLUMN2_NAME_DATA_CUMPRIDA +", " +FeedEntry.COLUMN2_NAME_DESCRICAO_META +", " +
            FeedEntry.COLUMN2_NAME_TEMPO_EXCEDIDO + " )";
    public static final String SQL_DELETE_ENTRIES2 = "DROP TABLE IF EXISTS " +FeedEntry.TABLE2_NAME;

    public static final String SQL_CREATE_ENTRIES3 =
            "CREATE TABLE " +FeedEntry.TABLE3_NAME + " (" +
            FeedEntry.COLUMN3_NAME_DESCRICAO_META + ", " +FeedEntry.COLUMN3_NAME_NOME_ATIVIDADE +" )";
    public static final String SQL_DELETE_ENTRIES3 = "DROP TABLE IF EXISTS " +FeedEntry.TABLE3_NAME;

    public static final String SQL_CREATE_ENTRIES4 =
            "CREATE TABLE " +FeedEntry.TABLE4_NAME +" (" +
            FeedEntry.COLUMN4_NAME_NOME +", " +FeedEntry.COLUMN4_NAME_DURACAO +", " +
            FeedEntry.COLUMN4_NAME_QUALIDADE +", " +FeedEntry.COLUMN4_NAME_DATA
            +" )";
    public static final String SQL_DELETE_ENTRIES4 = "DROP TABLE IF EXISTS " +FeedEntry.TABLE4_NAME;
}
