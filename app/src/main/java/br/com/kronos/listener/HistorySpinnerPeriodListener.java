package br.com.kronos.listener;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.kronos.database.KronosDatabase;
import br.com.kronos.kronos.Atividade;
import br.com.kronos.kronos.HistoryActivity;
import br.com.kronos.kronos.adapters.HistoryAdapterListView;


public class HistorySpinnerPeriodListener implements OnItemSelectedListener {

    public KronosDatabase kronosDatabase;
    public List<Atividade> cumulativeActivityList;
    public HistoryAdapterListView historyAdapterListView;
    //public Badass bad;
    public HistoryActivity historyActivity;
    public ListView listView;

    public HistorySpinnerPeriodListener(HistoryActivity historyActivity,List<Atividade> cumulativeActivityList, KronosDatabase kronosDatabase,HistoryAdapterListView historyAdapterListView, ListView listView ){
        this.cumulativeActivityList = cumulativeActivityList;
        this.kronosDatabase = kronosDatabase;
        this.historyAdapterListView = historyAdapterListView;
        this.historyActivity = historyActivity;
        this.listView = listView;
        //bad = new Badass(context,cumulativeActivityList,kronosDatabase,historyAdapterListView,listView);
    }


    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        //Toast.makeText(parent.getContext(), "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(), Toast.LENGTH_SHORT).show();
        /*TODO
            1)subtract the dates acording to the need: yesterday, last month, last year
            Special note) might be interesting to create another class to spend less memory
        */
        Toast.makeText(parent.getContext(), "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(), Toast.LENGTH_SHORT).show();

        String periodSelected = parent.getItemAtPosition(pos).toString().toLowerCase();
        historyActivity.selectedTime(periodSelected);

        /*
        if(parent.getItemAtPosition(pos).toString().equalsIgnoreCase("yesterday")){
            bad.selectedTime("yesterday");
            bad.setTheMiracle();
        }
        else if(parent.getItemAtPosition(pos).toString().equalsIgnoreCase("last week")) {
            bad.selectedTime("last week");
            bad.setTheMiracle();
        }
        else if(parent.getItemAtPosition(pos).toString().equalsIgnoreCase("last month")) {
            bad.selectedTime("last month");
            bad.setTheMiracle();
        }
        else if(parent.getItemAtPosition(pos).toString().equalsIgnoreCase("last year")) {
            bad.selectedTime("last year");
            bad.setTheMiracle();
        }
        */
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        //maybe whe can let the last day as a default
    }
}
