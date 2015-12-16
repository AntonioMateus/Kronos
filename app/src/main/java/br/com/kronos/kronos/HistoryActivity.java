package br.com.kronos.kronos;

import android.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import br.com.kronos.database.KronosDatabase;
import br.com.kronos.kronos.adapters.HistoryAdapterListView;
import br.com.kronos.kronos.adapters.HistoryListViewItem;
import br.com.kronos.listener.HistorySpinnerPeriodListener;

public class HistoryActivity extends Activity {

    public KronosDatabase kronosDatabase;

    public HistoryAdapterListView historyAdapterListView;
    public List<HistoryListViewItem> itens;
    public List<Atividade> cumulativeActivityList;
    public ListView listView;

    Date today = new java.util.Date();
    long oneDay = 86400000l; //1 day in milliseconds
    long oneWeek = 604800000l; //1 week in milliseconds
    long oneMonth = 2592000000l; //1 month in milliseconds
    long oneYear = 315360000000000l; //1 year in milliseconds

    long diff;
    Date searchDate;
    DateFormat df = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH);
    String date;

    //private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //creates de database and the list
        listView = (ListView) findViewById(R.id.listView_history_activities);
        kronosDatabase = new KronosDatabase(this);
        cumulativeActivityList = new LinkedList<>();
        itens = new LinkedList<>();

        //"creates" de spinner
        Spinner period = (Spinner) findViewById(R.id.spinner_selectPeriod);
        //get the spinner itens from the string array
        String[] period_options = getResources().getStringArray(R.array.spinner_period_options);
        //create the adapter
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, period_options);
        //set the listener
        period.setOnItemSelectedListener(new HistorySpinnerPeriodListener(this,cumulativeActivityList, kronosDatabase,historyAdapterListView,listView));
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

    public void selectedTime(String periodSelected){
        long forLimit = 0;
        switch (periodSelected) {
            case "yesterday":
                forLimit = oneDay;
                break;
            case "last week":
                forLimit = oneWeek;
                break;
            case "last month":
                forLimit = oneMonth;
                break;
            case "last year":
                forLimit = oneYear;
                break;
        }
        /*
        if(periodSelected.equalsIgnoreCase("yesterday")){
            forLimit = oneDay;
        }
        else if(periodSelected.equalsIgnoreCase("last week")){
            forLimit = oneWeek;
        }
        else if(periodSelected.equalsIgnoreCase("last month")){
            forLimit = oneMonth;
        }
        else if(periodSelected.equalsIgnoreCase("last year")){
            forLimit = oneDay*365;
        }
        */

        //clean the memory
        //cumulativeActivityList.removeAll(cumulativeActivityList);
        cumulativeActivityList = new LinkedList<>();
        //days that the user used the app, useful for showing the days of use
        //^not done yet, might create a class for it
        //number of days that the app was used
        int totalDays = 0;

        //for that goes day by day
        for (long daysIterator = oneDay; daysIterator <= forLimit; daysIterator += oneDay) {
            diff = today.getTime() - daysIterator;
            searchDate = new Date(diff);
            date = df.format(searchDate);
            int day = Integer.parseInt(date.substring(0, 2));
            int month = Integer.parseInt(date.substring(2, 4));
            int year = Integer.parseInt(date.substring(4, 8));
            //gets the list of a specific day

            //this temporary list stores the activities of a day
            List<Atividade> tempActivityList = kronosDatabase.getAtividadesHistorico(day, month, year);

            //if the list is not empty means that the app was used
            if ( !tempActivityList.isEmpty() ) {
                totalDays = totalDays + 1;
                //iterates on the activities of a day
                for (Atividade activity : tempActivityList) {
                    //if its new stores in a list called cumulative
                    if (!cumulativeActivityList.contains(activity)) {
                        double duration = activity.getDuracao();
                        double quality = activity.getQualidade();
                        String name = activity.getNome();
                        Atividade newActivity = new Atividade(name, duration, quality, day, month, year);
                        cumulativeActivityList.add(newActivity);
                    } else {
                        //if already exists, acummulate
                        int index = cumulativeActivityList.indexOf(activity);
                        Atividade cumuAct = cumulativeActivityList.get(index);
                        double cumuDuration = cumuAct.getDuracao();
                        double cumuQuality = cumuAct.getQualidade();
                        double tempDuration = activity.getDuracao();
                        double tempQuality = activity.getQualidade();
                        cumuAct.setDuracao(cumuDuration + tempDuration);
                        cumuAct.setQualidade(cumuQuality + tempQuality);
                    }
                }
            }
        }

        //divide the result by the number of the days that the app was used
        for(Atividade cumuActivity : cumulativeActivityList){
            double changeDuration = cumuActivity.getDuracao()/totalDays;
            double changeQuality = cumuActivity.getQualidade()/totalDays;
            cumuActivity.setDuracao(changeDuration);
            cumuActivity.setQualidade(changeQuality);
            //and add it to the adapter so that we can expand the list
            HistoryListViewItem listItem = new HistoryListViewItem(cumuActivity.getNome(),cumuActivity.getQualidade(),cumuActivity.getDuracao());
            itens.add(listItem);
        }

        HistoryAdapterListView adapterListView = new HistoryAdapterListView(this, itens);
        listView.setAdapter(adapterListView);//Alimentar a lista de atividades com o Adapter
    }
}
