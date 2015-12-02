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


    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_SHORT).show();

        /*TODO
            1)subtract the dates acording to the need: yesterday, last month, last year
            Special note) might be interesting to create another class to spend less memory
        */
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        //maybe whe can let the last day as a default
    }
}
