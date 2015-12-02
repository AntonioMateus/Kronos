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
import br.com.kronos.kronos.HistoryActivity;

public class HistorySpinnerPeriodListener extends HistoryActivity implements OnItemSelectedListener {


    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_SHORT).show();

        /*TODO
            1)subtract the dates acording to the need: yesterday, last month, last year
            Special note) might be interesting to create another class to spend less memory
        */

        Date today = new java.util.Date();
        long oneDay = 86400000l; //1 day in milliseconds
        long oneMonth = 2592000000l; //1 month in milliseconds
        long oneYear = 315360000000000l; //1 year in milliseconds

        long diff;
        Date searchDate;
        DateFormat df = new SimpleDateFormat("ddMMyyyy");
        String date;

        if(parent.getItemAtPosition(pos).toString().equalsIgnoreCase("yesterday")) {
            onYesterday();
        }
        if(parent.getItemAtPosition(pos).toString().equalsIgnoreCase("last month")) {
            onLastMonth();
        }
        if(parent.getItemAtPosition(pos).toString().equalsIgnoreCase("last year")) {
            /*
            diff = today.getTime()-oneYear;
            searchDate = new Date(diff);
            date = df.format(searchDate);
            day = Integer.parseInt(date.substring(0,2));
            month = Integer.parseInt(date.substring(2,4));
            year = Integer.parseInt(date.substring(4, 8));
            */
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        //maybe whe can let the last day as a default
    }
}
