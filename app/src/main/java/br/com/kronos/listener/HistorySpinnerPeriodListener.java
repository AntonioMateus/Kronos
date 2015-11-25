package br.com.kronos.listener;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import br.com.kronos.database.KronosDatabase;
import br.com.kronos.kronos.Atividade;

public class HistorySpinnerPeriodListener implements OnItemSelectedListener {

    public KronosDatabase kronosDatabase;
    public List<Atividade> activityList;

    public HistorySpinnerPeriodListener(List<Atividade> activityList, KronosDatabase kronosDatabase){
        this.activityList = activityList;
        this.kronosDatabase = kronosDatabase;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_SHORT).show();

        Calendar today = Calendar.getInstance();
        int day = today.get(Calendar.DAY_OF_MONTH);
        int month = today.get(Calendar.MONTH) + 1;
        int year = today.get(Calendar.YEAR);

        activityList = kronosDatabase.getAtividadesHistorico(day, month, year);
        /*TODO
            1)subtrct the dates acording to the need: yesterday, last month, last year
        */
        try {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            Date date1 = new java.util.Date();
            Date date2 = df.parse("01.01.2013");
            long diff = date2.getTime() - date1.getTime();
            Log.e("TEST" , date1.getTime() + " - " + date2.getTime() + " - " + diff);
        } catch (ParseException e) {
            Log.e("TEST", "Exception", e);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        //maybe whe can let the last day as a default
    }
}
