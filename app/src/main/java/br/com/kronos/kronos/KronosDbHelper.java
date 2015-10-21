package br.com.kronos.kronos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by antonio on 21/10/15.
 */
public class KronosDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Kronos";

    public KronosDbHelper (Context context) {
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

    public void Downgrade (SQLiteDatabase bd, int oldVersion, int newVersion) {
        onUpgrade(bd, oldVersion, newVersion);
    }
}