package br.com.kronos.listener;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import java.util.Calendar;
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
        //creates the linked list and the database
        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_SHORT).show();

        Calendar today = Calendar.getInstance();
        int day = today.get(Calendar.DAY_OF_MONTH);
        int month = today.get(Calendar.MONTH) + 1;
        int year = today.get(Calendar.YEAR);

        activityList = kronosDatabase.getAtividadesHistorico(day, month, year);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        //maybe whe can let the last day as a default
    }
}
