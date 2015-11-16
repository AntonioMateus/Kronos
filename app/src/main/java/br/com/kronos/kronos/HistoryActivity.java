package br.com.kronos.kronos;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ListView;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.PieChart;

import java.util.List;

import br.com.kronos.database.KronosDatabase;
import br.com.kronos.kronos.adapters.ListAtividadesAdapter;

public class HistoryActivity extends Activity {

    private KronosDatabase kronosDatabase;
    private Spinner period;
    private Spinner format;

    //private ListHistoryActivitiesAdapter listAtividadesAdapter;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        kronosDatabase = new KronosDatabase(this);


    }

}
