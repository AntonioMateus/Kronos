package br.com.kronos.database;

import android.provider.BaseColumns;

public class KronosContract {
    public KronosContract() {}

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_HISTORICO_NAME = "HistoricoDeAtividades";
        public static final String COLUMN_HISTORICO_NAME_NOME = "nome";
        public static final String COLUMN_HISTORICO_NAME_DURACAO = "duracao";
        public static final String COLUMN_HISTORICO_NAME_QUALIDADE = "qualidade";
        public static final String COLUMN_HISTORICO_NAME_DATA = "data";

        public static final String TABLE_LISTA_NAME = "ListaDeAtividades";
        public static final String COLUMN_LISTA_NAME_NOME = "nome";
        public static final String COLUMN_LISTA_NAME_DURACAO = "duracao";
        public static final String COLUMN_LISTA_NAME_QUALIDADE = "qualidade";

        public static final String TABLE_META_NAME = "Meta";
        public static final String COLUMN_META_NAME_DESCRICAO = "descricao";
        public static final String COLUMN_META_NAME_PRAZO = "prazo";
        public static final String COLUMN_META_NAME_TEMPO_ACUMULADO = "tempoAcumulado";
        public static final String COLUMN_META_NAME_TEMPO_ESTIPULADO = "tempoEstipulado";
        public static final String COLUMN_META_NAME_DATA_INICIO = "dataInicio";
        public static final String COLUMN_META_NAME_REPETIR = "repetir";
        public static final String COLUMN_META_NAME_CATEGORIA = "categoria";

        public static final String TABLE_META_CUMPRIDA_NAME = "MetaCumprida";
        public static final String COLUMN_META_CUMPRIDA_NAME_DATA_CUMPRIDA = "dataCumprida";
        public static final String COLUMN_META_CUMPRIDA_NAME_TEMPO_EXCEDIDO = "tempoExcedido";
        public static final String COLUMN_META_CUMPRIDA_NAME_DESCRICAO_META = "descricaoDaMeta";
    }

    public static final String SQL_CREATE_ENTRIES1 =
            "CREATE TABLE " + FeedEntry.TABLE_HISTORICO_NAME + " (" +
                    FeedEntry.COLUMN_HISTORICO_NAME_NOME + ", " + FeedEntry.COLUMN_HISTORICO_NAME_DURACAO + ", " +
                    FeedEntry.COLUMN_HISTORICO_NAME_QUALIDADE + ", " + FeedEntry.COLUMN_HISTORICO_NAME_DATA + " )";

    public static final String SQL_DELETE_ENTRIES1 = "DROP TABLE IF EXISTS " +FeedEntry.TABLE_HISTORICO_NAME;

    public static final String SQL_CREATE_ENTRIES2 =
            "CREATE TABLE " + FeedEntry.TABLE_LISTA_NAME + " (" +
                    FeedEntry.COLUMN_LISTA_NAME_NOME + ", " + FeedEntry.COLUMN_LISTA_NAME_DURACAO + ", " +
                    FeedEntry.COLUMN_LISTA_NAME_QUALIDADE + " )";

    public static final String SQL_DELETE_ENTRIES2 = "DROP TABLE IF EXISTS " +FeedEntry.TABLE_LISTA_NAME;

    public static final String SQL_CREATE_ENTRIES3 =
            "CREATE TABLE " +FeedEntry.TABLE_META_NAME + " (" +
                    FeedEntry.COLUMN_META_NAME_DESCRICAO +", " +FeedEntry.COLUMN_META_NAME_PRAZO + ", " +
                    FeedEntry.COLUMN_META_NAME_TEMPO_ACUMULADO +", " +FeedEntry.COLUMN_META_NAME_TEMPO_ESTIPULADO +", " +
                    FeedEntry.COLUMN_META_NAME_DATA_INICIO +", " +FeedEntry.COLUMN_META_NAME_REPETIR +", " +
                    FeedEntry.COLUMN_META_NAME_CATEGORIA + ", PRIMARY KEY (" + FeedEntry.COLUMN_META_NAME_DESCRICAO +
                    ") )";

    public static final String SQL_DELETE_ENTRIES3 = "DROP TABLE IF EXISTS " +FeedEntry.TABLE_META_NAME;

    public static final String SQL_CREATE_ENTRIES4 =
            "CREATE TABLE " +FeedEntry.TABLE_META_CUMPRIDA_NAME + " (" +
                    FeedEntry.COLUMN_META_CUMPRIDA_NAME_DATA_CUMPRIDA +", " +FeedEntry.COLUMN_META_CUMPRIDA_NAME_TEMPO_EXCEDIDO+
                    ", " +FeedEntry.COLUMN_META_CUMPRIDA_NAME_DESCRICAO_META +", PRIMARY KEY (" +FeedEntry.COLUMN_META_CUMPRIDA_NAME_DATA_CUMPRIDA +
                    "), FOREIGN KEY ("+FeedEntry.COLUMN_META_CUMPRIDA_NAME_DESCRICAO_META+
                    ") REFERENCES " +FeedEntry.TABLE_META_NAME +" (" +FeedEntry.COLUMN_META_NAME_DESCRICAO +"))";

    public static final String SQL_DELETE_ENTRIES4 = "DROP TABLE IF EXISTS " +FeedEntry.TABLE_META_CUMPRIDA_NAME;
}
