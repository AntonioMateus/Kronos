package br.com.kronos.kronos;

import android.os.Bundle;
import android.app.Activity;

public class HistoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
