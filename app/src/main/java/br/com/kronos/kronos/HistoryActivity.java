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

        //creates de database and the list
        kronosDatabase = new KronosDatabase(this);
        activityList = new LinkedList<>();

        //"creates" de spinner
        period = (Spinner) findViewById(R.id.spinner_selectPeriod);
        //get the spinner itens from the string array
        String[] period_options = getResources().getStringArray(R.array.spinner_period_options);
        //create the adapter
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, period_options);
        //set the listener
        period.setOnItemSelectedListener(new HistorySpinnerPeriodListener(activityList, kronosDatabase));
        //set the view for the dropdown. Remember that you might have to use some attr from the simple spinner
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        //set the adapter
        period.setAdapter(dataAdapter);

        /*TODO
              1)test the spinner item (we need a "textView", in this case checkedTextView
              to change the size of the text) we might need to get a simple spinner from
              the android default
              2)the plot
              3)the plot options: average and absolute value
              4)format option: days and hours
              5)the activity list
              6)the goals list

        */
    }

}
