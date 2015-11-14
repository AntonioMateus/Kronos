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
    }

    public static final String SQL_CREATE_ENTRIES1 =
            "CREATE TABLE IF NOT EXIST" + FeedEntry.TABLE_HISTORICO_NAME + " (" +
                    FeedEntry.COLUMN_HISTORICO_NAME_NOME + ", " + FeedEntry.COLUMN_HISTORICO_NAME_DURACAO + ", " +
                    FeedEntry.COLUMN_HISTORICO_NAME_QUALIDADE + ", " + FeedEntry.COLUMN_HISTORICO_NAME_DATA + " )";

    public static final String SQL_DELETE_ENTRIES1 = "DROP TABLE IF EXISTS " +FeedEntry.TABLE_HISTORICO_NAME;

    public static final String SQL_CREATE_ENTRIES2 =
            "CREATE TABLE IF NOT EXIST" + FeedEntry.TABLE_LISTA_NAME + " (" +
                    FeedEntry.COLUMN_LISTA_NAME_NOME + ", " + FeedEntry.COLUMN_LISTA_NAME_DURACAO + ", " +
                    FeedEntry.COLUMN_LISTA_NAME_QUALIDADE + " )";

    public static final String SQL_DELETE_ENTRIES2 = "DROP TABLE IF EXISTS " +FeedEntry.TABLE_LISTA_NAME;
}
