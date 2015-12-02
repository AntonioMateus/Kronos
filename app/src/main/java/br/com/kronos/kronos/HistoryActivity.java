package br.com.kronos.kronos;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.PieChart;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import br.com.kronos.database.KronosDatabase;
import br.com.kronos.kronos.adapters.ListAtividadesAdapter;
import br.com.kronos.listener.HistorySpinnerPeriodListener;

public class HistoryActivity extends Activity {

    public KronosDatabase kronosDatabase;
    private Spinner period;
    private Spinner format;
    private int day;
    private int month;
    private int year;
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
        period.setOnItemSelectedListener(new HistorySpinnerPeriodListener());
        //set the view for the dropdown. Remember that you might have to use some attr from the simple spinner
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        //set the adapter
        period.setAdapter(dataAdapter);

        Date today = new java.util.Date();
        long oneDay = 86400000l; //1 day in milliseconds
        long oneMonth = 2592000000l; //1 month in milliseconds
        long oneYear = 315360000000000l; //1 year in milliseconds
        long diff;
        Date searchDate;
        DateFormat df = new SimpleDateFormat("ddMMyyyy");
        String date;

        /*
        if(parent.getItemAtPosition(pos).toString().equalsIgnoreCase("yesterday")) {
            diff = today.getTime()-oneDay;
            searchDate = new Date(diff);
            date = df.format(searchDate);
            day = Integer.parseInt(date.substring(0,2));
            month = Integer.parseInt(date.substring(2, 4));
            year = Integer.parseInt(date.substring(4,8));
            activityList.removeAll(activityList);
            activityList = kronosDatabase.getAtividadesHistorico(day, month, year);
        }
        if(parent.getItemAtPosition(pos).toString().equalsIgnoreCase("last month")) {
            activityList.removeAll(activityList);
            for(long daysIterator = oneDay; daysIterator<oneMonth; daysIterator+=oneDay){
                diff = today.getTime() - daysIterator;
                searchDate = new Date(diff);
                date = df.format(searchDate);
                day = Integer.parseInt(date.substring(0, 2));
                month = Integer.parseInt(date.substring(2, 4));
                year = Integer.parseInt(date.substring(4, 8));
                tempActivityList = kronosDatabase.getAtividadesHistorico(day, month,year);
                for(Atividade activities : tempActivityList){
                    if(!activityList.contains(activities)){
                        //creates another atividade
                        //setting the falues to 1/30 to avoid iterating another time
                        double duration = activities.getDuracao();
                        double quality = activities.getQualidade();
                        String name = activities.getNome();
                        Atividade newActivity = new Atividade(name,duration,quality,day,month,year);
                        activityList.add(newActivity);
                    }
                }
            }
        }
        if(parent.getItemAtPosition(pos).toString().equalsIgnoreCase("last year")) {
            diff = today.getTime()-oneYear;
            searchDate = new Date(diff);
            date = df.format(searchDate);
            day = Integer.parseInt(date.substring(0,2));
            month = Integer.parseInt(date.substring(2,4));
            year = Integer.parseInt(date.substring(4, 8));
        }
        */

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
