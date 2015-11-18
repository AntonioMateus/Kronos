package br.com.kronos.kronos;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.PieChart;

import java.util.LinkedList;
import java.util.List;

import br.com.kronos.database.KronosDatabase;
import br.com.kronos.kronos.adapters.ListAtividadesAdapter;
import br.com.kronos.listener.HistorySpinnerPeriodListener;

public class HistoryActivity extends Activity {

    public KronosDatabase kronosDatabase;
    private Spinner period;
    private Spinner format;
    public List<Atividade> activityList;

    //private ListHistoryActivitiesAdapter listAtividadesAdapter;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        kronosDatabase = new KronosDatabase(this);
        activityList = new LinkedList<>();

        period = (Spinner) findViewById(R.id.spinner_selectPeriod);
        String[] period_options = getResources().getStringArray(R.array.spinner_period_options);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, period_options);
        period.setOnItemSelectedListener(new HistorySpinnerPeriodListener(activityList, kronosDatabase));
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
    }

}
