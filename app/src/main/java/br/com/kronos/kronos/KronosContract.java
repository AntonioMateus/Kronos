package br.com.kronos.kronos;

import android.provider.BaseColumns;

/**
 * Created by antonio on 21/10/15.
 */
public class KronosContract {
    public KronosContract() {}

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE1_NAME = "HistoricoDeAtividades";
        public static final String COLUMN1_NAME_NOME = "nome";
        public static final String COLUMN1_NAME_DURACAO = "duracao";
        public static final String COLUMN1_NAME_QUALIDADE = "qualidade";
        public static final String COLUMN1_NAME_DATA = "data";

        public static final String TABLE2_NAME = "ListaDeAtividades";
        public static final String COLUMN2_NAME_NOME = "nome";
        public static final String COLUMN2_NAME_DURACAO = "duracao";
        public static final String COLUMN2_NAME_QUALIDADE = "qualidade";
    }

    public static final String SQL_CREATE_ENTRIES1 =
            "CREATE TABLE " +FeedEntry.TABLE1_NAME + " (" +
            FeedEntry.COLUMN1_NAME_NOME +", " +FeedEntry.COLUMN1_NAME_DURACAO +", "
            +FeedEntry.COLUMN1_NAME_QUALIDADE +", " +FeedEntry.COLUMN1_NAME_DATA + " )";
    public static final String SQL_DELETE_ENTRIES1 = "DROP TABLE IF EXISTS " +FeedEntry.TABLE1_NAME;

    public static final String SQL_CREATE_ENTRIES2 =
            "CREATE TABLE " +FeedEntry.TABLE2_NAME + " (" +
            FeedEntry.COLUMN2_NAME_NOME +", " +FeedEntry.COLUMN2_NAME_DURACAO +", " +
            FeedEntry.COLUMN2_NAME_QUALIDADE + " )";
    public static final String SQL_DELETE_ENTRIES2 = "DROP TABLE IF EXISTS " +FeedEntry.TABLE2_NAME;
}
